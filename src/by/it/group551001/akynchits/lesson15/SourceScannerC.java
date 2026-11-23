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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SourceScannerC {

    private static final int COPY_THRESHOLD = 10; // копиями считаются тексты с числом правок < 10
    private static final int MAX_DIST = COPY_THRESHOLD - 1;
    private static final int INF = COPY_THRESHOLD;

    private static class FileData {
        final String relativePath;
        final String content;

        FileData(String relativePath, String content) {
            this.relativePath = relativePath;
            this.content = content;
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

        List<Path> javaFiles;
        try (Stream<Path> paths = Files.walk(srcPath)) {
            javaFiles = paths.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Параллельное чтение и очистка текстов
        List<FileData> files = javaFiles.parallelStream()
                .map(p -> processFile(srcPath, p))
                .filter(data -> data != null)
                .sorted(Comparator.comparingInt((FileData f) -> f.content.length())
                        .thenComparing(f -> f.relativePath))
                .collect(Collectors.toList());

        int count = files.size();
        List<List<String>> copies = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            copies.add(Collections.synchronizedList(new ArrayList<>()));
        }

        // Переиспользуемые буферы на поток для полосового алгоритма Левенштейна
        ThreadLocal<int[][]> buffers = ThreadLocal.withInitial(() -> new int[2][]);

        // Поскольку файлы отсортированы по длине, сравниваем только пары с разницей длин < 10
        IntStream.range(0, count).parallel().forEach(i -> {
            FileData first = files.get(i);
            String s1 = first.content;
            int len1 = s1.length();

            for (int j = i + 1; j < count; j++) {
                FileData second = files.get(j);
                String s2 = second.content;
                int len2 = s2.length();

                if (len2 - len1 >= COPY_THRESHOLD) {
                    break;
                }

                if (isCopy(s1, s2, buffers)) {
                    copies.get(i).add(second.relativePath);
                    copies.get(j).add(first.relativePath);
                }
            }
        });

        // Сортируем файлы с копиями лексикографически по их пути для вывода
        List<Integer> indicesWithCopies = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (!copies.get(i).isEmpty()) {
                indicesWithCopies.add(i);
            }
        }
        indicesWithCopies.sort(Comparator.comparing(i -> files.get(i).relativePath));

        StringBuilder out = new StringBuilder();
        for (int idx : indicesWithCopies) {
            out.append(files.get(idx).relativePath).append('\n');
            List<String> fileCopies = copies.get(idx);
            Collections.sort(fileCopies);
            for (String copyPath : fileCopies) {
                out.append(copyPath).append('\n');
            }
        }
        System.out.print(out);
    }

    private static FileData processFile(Path srcPath, Path filePath) {
        String text = readFileIgnoringErrors(filePath);
        if (text == null) {
            return null;
        }

        if (text.contains("@Test") || text.contains("org.junit.Test")) {
            return null;
        }

        String cleaned = removeComments(text);
        cleaned = removePackageAndImports(cleaned);
        cleaned = normalizeWhitespaceAndTrim(cleaned);

        String relativePath = srcPath.relativize(filePath).toString();
        return new FileData(relativePath, cleaned);
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

    private static boolean isCopy(String a, String b, ThreadLocal<int[][]> buffers) {
        int lenA = a.length();
        int lenB = b.length();

        if (Math.abs(lenA - lenB) > MAX_DIST) {
            return false;
        }
        if (a.equals(b)) {
            return true;
        }

        // Отрезаем совпадающие префикс и суффикс без перекрытия
        int minLen = Math.min(lenA, lenB);
        int prefix = 0;
        while (prefix < minLen && a.charAt(prefix) == b.charAt(prefix)) {
            prefix++;
        }

        int suffix = 0;
        int limit = minLen - prefix;
        while (suffix < limit && a.charAt(lenA - 1 - suffix) == b.charAt(lenB - 1 - suffix)) {
            suffix++;
        }

        int aStart = prefix;
        int aEnd = lenA - suffix;
        int bStart = prefix;
        int bEnd = lenB - suffix;

        int n = aEnd - aStart;
        int m = bEnd - bStart;

        if (n == 0) {
            return m <= MAX_DIST;
        }
        if (m == 0) {
            return n <= MAX_DIST;
        }
        if (Math.abs(n - m) > MAX_DIST) {
            return false;
        }

        // Делаем первую строку не длиннее второй (для минимизации числа шагов)
        if (n > m) {
            return bandedLevenshtein(b, bStart, bEnd, a, aStart, aEnd, buffers) < COPY_THRESHOLD;
        }
        return bandedLevenshtein(a, aStart, aEnd, b, bStart, bEnd, buffers) < COPY_THRESHOLD;
    }

    private static int bandedLevenshtein(String a, int aStart, int aEnd,
                                         String b, int bStart, int bEnd,
                                         ThreadLocal<int[][]> buffers) {
        int n = aEnd - aStart;
        int m = bEnd - bStart;

        int[][] bufs = buffers.get();
        if (bufs[0] == null || bufs[0].length < m + 1) {
            int newSize = Math.max(m + 1, 256);
            bufs[0] = new int[newSize];
            bufs[1] = new int[newSize];
        }

        int[] prev = bufs[0];
        int[] curr = bufs[1];

        int initLimit = Math.min(m, MAX_DIST);
        for (int j = 0; j <= initLimit; j++) {
            prev[j] = j;
        }
        if (MAX_DIST + 1 <= m) {
            prev[MAX_DIST + 1] = INF;
        }

        for (int i = 1; i <= n; i++) {
            int from = Math.max(1, i - MAX_DIST);
            int to = Math.min(m, i + MAX_DIST);

            if (from == 1) {
                curr[0] = i;
            } else {
                curr[from - 1] = INF;
            }

            if (i + MAX_DIST <= m) {
                prev[i + MAX_DIST] = INF;
            }

            char ca = a.charAt(aStart + i - 1);
            int minInRow = INF;

            for (int j = from; j <= to; j++) {
                int cost = (ca == b.charAt(bStart + j - 1)) ? 0 : 1;
                int val = Math.min(
                        Math.min(prev[j] + 1, curr[j - 1] + 1),
                        prev[j - 1] + cost
                );
                curr[j] = val;
                if (val < minInRow) {
                    minInRow = val;
                }
            }

            if (minInRow > MAX_DIST) {
                return INF;
            }

            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return prev[m];
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

    private static String normalizeWhitespaceAndTrim(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        boolean inSpace = true; // пропускает ведущие символы с кодом < 33

        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!inSpace) {
                    sb.append(' ');
                    inSpace = true;
                }
            } else {
                sb.append(c);
                inSpace = false;
            }
        }

        int len = sb.length();
        if (len > 0 && sb.charAt(len - 1) == ' ') {
            sb.setLength(len - 1);
        }

        return sb.toString();
    }
}
