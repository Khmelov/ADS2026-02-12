package by.it.group551001.akynchits.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerB {

    private static class FileEntry {
        final int size;
        final String relativePath;

        FileEntry(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }

    private enum State {
        NORMAL,
        IN_STRING,
        IN_TEXT_BLOCK,
        IN_CHAR,
        IN_LINE_COMMENT,
        IN_BLOCK_COMMENT
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath) || !Files.isDirectory(srcPath)) {
            return;
        }

        List<FileEntry> entries = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(srcPath)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .forEach(p -> processFile(srcPath, p, entries));
        } catch (IOException e) {
            e.printStackTrace();
        }

        entries.sort(Comparator.comparingInt((FileEntry e) -> e.size)
                .thenComparing(e -> e.relativePath));

        for (FileEntry entry : entries) {
            System.out.println(entry.size + " " + entry.relativePath);
        }
    }

    private static void processFile(Path srcPath, Path filePath, List<FileEntry> entries) {
        String text = readFileIgnoringErrors(filePath);
        if (text == null) {
            return;
        }

        if (text.contains("@Test") || text.contains("org.junit.Test")) {
            return;
        }

        String cleaned = removeComments(text);
        cleaned = removePackageAndImports(cleaned);
        cleaned = removeEmptyLines(cleaned);
        cleaned = trimControlChars(cleaned);

        int byteSize = cleaned.getBytes(StandardCharsets.UTF_8).length;
        String relativePath = srcPath.relativize(filePath).toString();
        entries.add(new FileEntry(byteSize, relativePath));
    }

    private static String readFileIgnoringErrors(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.IGNORE)
                    .onUnmappableCharacter(CodingErrorAction.IGNORE);
            return decoder.decode(ByteBuffer.wrap(bytes)).toString();
        } catch (IOException e) {
            return null;
        }
    }

    private static String removeComments(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        State state = State.NORMAL;
        int i = 0;

        while (i < n) {
            char c = text.charAt(i);

            switch (state) {
                case NORMAL:
                    if (text.startsWith("\"\"\"", i)) {
                        sb.append("\"\"\"");
                        i += 3;
                        state = State.IN_TEXT_BLOCK;
                    } else if (c == '"') {
                        sb.append(c);
                        i++;
                        state = State.IN_STRING;
                    } else if (c == '\'') {
                        sb.append(c);
                        i++;
                        state = State.IN_CHAR;
                    } else if (c == '/' && i + 1 < n) {
                        char next = text.charAt(i + 1);
                        if (next == '/') {
                            i += 2;
                            state = State.IN_LINE_COMMENT;
                        } else if (next == '*') {
                            i += 2;
                            state = State.IN_BLOCK_COMMENT;
                        } else {
                            sb.append(c);
                            i++;
                        }
                    } else {
                        sb.append(c);
                        i++;
                    }
                    break;

                case IN_STRING:
                    if (c == '\\' && i + 1 < n) {
                        sb.append(c).append(text.charAt(i + 1));
                        i += 2;
                    } else if (c == '"') {
                        sb.append(c);
                        i++;
                        state = State.NORMAL;
                    } else {
                        sb.append(c);
                        i++;
                    }
                    break;

                case IN_TEXT_BLOCK:
                    if (c == '\\' && i + 1 < n) {
                        sb.append(c).append(text.charAt(i + 1));
                        i += 2;
                    } else if (text.startsWith("\"\"\"", i)) {
                        sb.append("\"\"\"");
                        i += 3;
                        state = State.NORMAL;
                    } else {
                        sb.append(c);
                        i++;
                    }
                    break;

                case IN_CHAR:
                    if (c == '\\' && i + 1 < n) {
                        sb.append(c).append(text.charAt(i + 1));
                        i += 2;
                    } else if (c == '\'') {
                        sb.append(c);
                        i++;
                        state = State.NORMAL;
                    } else {
                        sb.append(c);
                        i++;
                    }
                    break;

                case IN_LINE_COMMENT:
                    if (c == '\n') {
                        sb.append('\n');
                        state = State.NORMAL;
                    }
                    i++;
                    break;

                case IN_BLOCK_COMMENT:
                    if (c == '*' && i + 1 < n && text.charAt(i + 1) == '/') {
                        i += 2;
                        state = State.NORMAL;
                    } else {
                        i++;
                    }
                    break;
            }
        }

        return sb.toString();
    }

    private static String removePackageAndImports(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        int i = 0;

        while (i < n) {
            int lineStart = i;
            int lineEnd = text.indexOf('\n', i);
            int nextLineStart;
            if (lineEnd == -1) {
                lineEnd = n;
                nextLineStart = n;
            } else {
                nextLineStart = lineEnd + 1;
            }

            int pos = lineStart;
            while (pos < lineEnd && (text.charAt(pos) == ' ' || text.charAt(pos) == '\t')) {
                pos++;
            }

            boolean isPackage = text.startsWith("package", pos)
                    && pos + 7 < lineEnd && Character.isWhitespace(text.charAt(pos + 7));
            boolean isImport = text.startsWith("import", pos)
                    && pos + 6 < lineEnd && Character.isWhitespace(text.charAt(pos + 6));

            if (!isPackage && !isImport) {
                sb.append(text, lineStart, nextLineStart);
            }

            i = nextLineStart;
        }

        return sb.toString();
    }

    private static String removeEmptyLines(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        int i = 0;

        while (i < n) {
            int lineStart = i;
            int lineEnd = text.indexOf('\n', i);
            int nextLineStart;
            if (lineEnd == -1) {
                lineEnd = n;
                nextLineStart = n;
            } else {
                nextLineStart = lineEnd + 1;
            }

            boolean hasVisibleChars = false;
            for (int j = lineStart; j < lineEnd; j++) {
                if (text.charAt(j) >= 33) {
                    hasVisibleChars = true;
                    break;
                }
            }

            if (hasVisibleChars) {
                sb.append(text, lineStart, nextLineStart);
            }

            i = nextLineStart;
        }

        return sb.toString();
    }

    private static String trimControlChars(String text) {
        int start = 0;
        int end = text.length();

        while (start < end && text.charAt(start) < 33) {
            start++;
        }
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        return text.substring(start, end);
    }
}
