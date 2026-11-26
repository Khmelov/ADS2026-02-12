package by.it.group551002.voroshko.lesson15;


import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class SourceScannerB {

    private static class FileData {
        final int size;
        final String path;

        FileData(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }

    private enum State {
        NORMAL,
        LINE_COMMENT,
        BLOCK_COMMENT,
        STRING,
        CHAR,
        TEXT_BLOCK
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath)) {
            return;
        }

        List<Path> javaFiles = new ArrayList<>(4096);
        try {
            Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (attrs.isRegularFile() && file.getFileName().toString().endsWith(".java")) {
                        javaFiles.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<FileData> results = processInParallel(srcPath, javaFiles);

        results.sort(Comparator
                .comparingInt((FileData f) -> f.size)
                .thenComparing(f -> f.path));

        StringBuilder out = new StringBuilder(results.size() * 64);
        String nl = System.lineSeparator();
        for (FileData data : results) {
            out.append(data.size).append(' ').append(data.path).append(nl);
        }
        System.out.print(out);
    }

    private static List<FileData> processInParallel(Path srcPath, List<Path> javaFiles) {
        if (javaFiles.isEmpty()) {
            return new ArrayList<>();
        }

        int workers = Math.min(16, Math.max(4, Runtime.getRuntime().availableProcessors() * 2));
        ExecutorService pool = Executors.newFixedThreadPool(workers);
        AtomicInteger index = new AtomicInteger(0);
        List<Future<List<FileData>>> futures = new ArrayList<>(workers);

        try {
            for (int w = 0; w < workers; w++) {
                futures.add(pool.submit(() -> {
                    CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                            .onMalformedInput(CodingErrorAction.IGNORE)
                            .onUnmappableCharacter(CodingErrorAction.IGNORE);
                    List<FileData> localResults = new ArrayList<>();
                    int i;
                    while ((i = index.getAndIncrement()) < javaFiles.size()) {
                        FileData data = processFile(srcPath, javaFiles.get(i), decoder);
                        if (data != null) {
                            localResults.add(data);
                        }
                    }
                    return localResults;
                }));
            }

            List<FileData> allResults = new ArrayList<>(javaFiles.size());
            for (Future<List<FileData>> future : futures) {
                allResults.addAll(future.get());
            }
            return allResults;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } finally {
            pool.shutdownNow();
        }
    }

    private static FileData processFile(Path srcPath, Path file, CharsetDecoder decoder) {
        String text;
        try {
            byte[] bytes = Files.readAllBytes(file);
            decoder.reset();
            text = decoder.decode(ByteBuffer.wrap(bytes)).toString();
        } catch (IOException e) {
            return null;
        }

        if (text.contains("@Test") || text.contains("org.junit.Test")) {
            return null;
        }

        String cleaned = cleanText(text);
        int byteSize = cleaned.getBytes(StandardCharsets.UTF_8).length;
        String relativePath = srcPath.relativize(file).toString();

        return new FileData(byteSize, relativePath);
    }

    private static String cleanText(String text) {
        StringBuilder noComments = removeComments(text);

        int n = noComments.length();
        StringBuilder result = new StringBuilder(n);
        int i = 0;

        while (i < n) {
            int lineStart = i;
            while (i < n && noComments.charAt(i) != '\n') {
                i++;
            }
            int lineEnd = i;
            if (i < n && noComments.charAt(i) == '\n') {
                i++;
            }

            int p = lineStart;
            while (p < lineEnd && noComments.charAt(p) < 33) {
                p++;
            }

            if (p == lineEnd) {
                continue;
            }

            if (startsWithWord(noComments, p, lineEnd, "package")
                    || startsWithWord(noComments, p, lineEnd, "import")) {
                continue;
            }

            result.append(noComments, lineStart, i);
        }

        int start = 0;
        int end = result.length();
        while (start < end && result.charAt(start) < 33) {
            start++;
        }
        while (end > start && result.charAt(end - 1) < 33) {
            end--;
        }

        return result.substring(start, end);
    }

    private static StringBuilder removeComments(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        State state = State.NORMAL;
        int i = 0;

        while (i < n) {
            char c = text.charAt(i);
            switch (state) {
                case NORMAL:
                    if (c == '"') {
                        if (i + 2 < n && text.charAt(i + 1) == '"' && text.charAt(i + 2) == '"') {
                            sb.append("\"\"\"");
                            state = State.TEXT_BLOCK;
                            i += 3;
                        } else {
                            sb.append('"');
                            state = State.STRING;
                            i++;
                        }
                    } else if (c == '\'') {
                        sb.append('\'');
                        state = State.CHAR;
                        i++;
                    } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '/') {
                        state = State.LINE_COMMENT;
                        i += 2;
                    } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '*') {
                        state = State.BLOCK_COMMENT;
                        i += 2;
                    } else {
                        sb.append(c);
                        i++;
                    }
                    break;

                case LINE_COMMENT:
                    if (c == '\r' || c == '\n') {
                        sb.append(c);
                        state = State.NORMAL;
                    }
                    i++;
                    break;

                case BLOCK_COMMENT:
                    if (c == '*' && i + 1 < n && text.charAt(i + 1) == '/') {
                        state = State.NORMAL;
                        i += 2;
                    } else {
                        i++;
                    }
                    break;

                case STRING:
                    sb.append(c);
                    if (c == '\\' && i + 1 < n) {
                        sb.append(text.charAt(i + 1));
                        i += 2;
                    } else {
                        if (c == '"' || c == '\r' || c == '\n') {
                            state = State.NORMAL;
                        }
                        i++;
                    }
                    break;

                case CHAR:
                    sb.append(c);
                    if (c == '\\' && i + 1 < n) {
                        sb.append(text.charAt(i + 1));
                        i += 2;
                    } else {
                        if (c == '\'' || c == '\r' || c == '\n') {
                            state = State.NORMAL;
                        }
                        i++;
                    }
                    break;

                case TEXT_BLOCK:
                    if (c == '\\' && i + 1 < n) {
                        sb.append(c).append(text.charAt(i + 1));
                        i += 2;
                    } else if (c == '"' && i + 2 < n && text.charAt(i + 1) == '"' && text.charAt(i + 2) == '"') {
                        sb.append("\"\"\"");
                        state = State.NORMAL;
                        i += 3;
                    } else {
                        sb.append(c);
                        i++;
                    }
                    break;
            }
        }

        return sb;
    }

    private static boolean startsWithWord(CharSequence cs, int offset, int lineEnd, String word) {
        int len = word.length();
        if (offset + len >= lineEnd) {
            return false;
        }
        for (int j = 0; j < len; j++) {
            if (cs.charAt(offset + j) != word.charAt(j)) {
                return false;
            }
        }
        return cs.charAt(offset + len) < 33;
    }
}