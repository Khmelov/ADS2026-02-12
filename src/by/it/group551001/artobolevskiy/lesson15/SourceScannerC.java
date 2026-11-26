package by.it.group551001.artobolevskiy.lesson15;

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

public class SourceScannerC {

    private static final int COPY_THRESHOLD = 10;

    static class FileEntry {
        final String path;
        final String text;
        final List<String> copies = new ArrayList<>();

        FileEntry(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath)) {
            return;
        }

        List<FileEntry> files = new ArrayList<>();

        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);

        try (Stream<Path> walk = Files.walk(srcPath)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            byte[] rawBytes = Files.readAllBytes(p);
                            String content = decoder.reset().decode(ByteBuffer.wrap(rawBytes)).toString();

                            // Пропускаем тестовые файлы
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // 1. Удаляем package и import
                            String noImports = removePackageAndImports(content);

                            // 2. Удаляем комментарии за O(n)
                            String noComments = removeComments(noImports);

                            // 3 & 4. Заменяем последовательности символов с кодом < 33 на один пробел (32) и делаем trim()
                            String normalized = normalizeWhitespace(noComments);

                            String relPath = srcPath.relativize(p).toString();
                            files.add(new FileEntry(relPath, normalized));
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортируем все файлы лексикографически по пути заранее
        files.sort(Comparator.comparing(f -> f.path));

        int n = files.size();
        // Перебираем все пары (i, j) и проверяем, является ли расстояние Левенштейна < 10
        for (int i = 0; i < n; i++) {
            FileEntry f1 = files.get(i);
            for (int j = i + 1; j < n; j++) {
                FileEntry f2 = files.get(j);

                // Быстрая отсечка по разнице длин строк
                if (Math.abs(f1.text.length() - f2.text.length()) >= COPY_THRESHOLD) {
                    continue;
                }

                int dist = boundedLevenshtein(f1.text, f2.text, COPY_THRESHOLD - 1);
                if (dist < COPY_THRESHOLD) {
                    f1.copies.add(f2.path);
                    f2.copies.add(f1.path);
                }
            }
        }

        // Выводим результаты в лексикографическом порядке
        for (FileEntry entry : files) {
            if (!entry.copies.isEmpty()) {
                System.out.println(entry.path);
                for (String copyPath : entry.copies) {
                    System.out.println(copyPath);
                }
            }
        }
    }

    // 1. Удаление package и import за O(n)
    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        while (i < len) {
            int lineStart = i;
            int lineEnd = text.indexOf('\n', i);
            int nextStart = (lineEnd == -1) ? len : lineEnd + 1;
            if (lineEnd == -1) {
                lineEnd = len;
            }

            int pos = lineStart;
            while (pos < lineEnd && text.charAt(pos) <= ' ') {
                pos++;
            }

            if (!startsWithWord(text, pos, lineEnd, "package") &&
                    !startsWithWord(text, pos, lineEnd, "import")) {
                sb.append(text, lineStart, nextStart);
            }

            i = nextStart;
        }
        return sb.toString();
    }

    private static boolean startsWithWord(String text, int pos, int lineEnd, String word) {
        int wordLen = word.length();
        if (lineEnd - pos < wordLen) {
            return false;
        }
        for (int j = 0; j < wordLen; j++) {
            if (text.charAt(pos + j) != word.charAt(j)) {
                return false;
            }
        }
        if (pos + wordLen < lineEnd) {
            char nextChar = text.charAt(pos + wordLen);
            return nextChar <= ' ' || nextChar == ';';
        }
        return true;
    }

    // 2. Удаление однострочных и многострочных комментариев за O(n)
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        boolean inString = false;
        boolean inChar = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;

        while (i < len) {
            char c = text.charAt(i);
            char next = (i + 1 < len) ? text.charAt(i + 1) : '\0';

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    sb.append(c);
                }
                i++;
            } else if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i += 2;
                } else {
                    i++;
                }
            } else if (inString) {
                sb.append(c);
                if (c == '\\' && i + 1 < len) {
                    sb.append(next);
                    i += 2;
                } else {
                    if (c == '"') {
                        inString = false;
                    }
                    i++;
                }
            } else if (inChar) {
                sb.append(c);
                if (c == '\\' && i + 1 < len) {
                    sb.append(next);
                    i += 2;
                } else {
                    if (c == '\'') {
                        inChar = false;
                    }
                    i++;
                }
            } else {
                if (c == '/' && next == '/') {
                    inLineComment = true;
                    i += 2;
                } else if (c == '/' && next == '*') {
                    inBlockComment = true;
                    i += 2;
                } else {
                    if (c == '"') {
                        inString = true;
                    } else if (c == '\'') {
                        inChar = true;
                    }
                    sb.append(c);
                    i++;
                }
            }
        }
        return sb.toString();
    }

    // 3 & 4. Замена последовательностей символов с кодом < 33 на один пробел (32) + trim() за O(n)
    private static String normalizeWhitespace(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        boolean prevSpace = true; // true в начале заменяет leading trim()

        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!prevSpace) {
                    sb.append((char) 32);
                    prevSpace = true;
                }
            } else {
                sb.append(c);
                prevSpace = false;
            }
        }

        // Убираем завершающий пробел (trailing trim())
        if (!sb.isEmpty() && sb.charAt(sb.length() - 1) == 32) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    // Оптимизированное расстояние Левенштейна с порогом (Ukkonen / Banded DP)
    private static int boundedLevenshtein(String s1, String s2, int maxDist) {
        int len1 = s1.length();
        int len2 = s2.length();

        if (Math.abs(len1 - len2) > maxDist) {
            return maxDist + 1;
        }

        // Пропускаем общий префикс
        int start = 0;
        while (start < len1 && start < len2 && s1.charAt(start) == s2.charAt(start)) {
            start++;
        }

        // Пропускаем общий суффикс
        while (len1 > start && len2 > start && s1.charAt(len1 - 1) == s2.charAt(len2 - 1)) {
            len1--;
            len2--;
        }

        int n = len1 - start;
        int m = len2 - start;

        if (n == 0) return m;
        if (m == 0) return n;
        if (Math.abs(n - m) > maxDist) return maxDist + 1;

        // Гарантируем, что n <= m, чтобы минимизировать потребление памяти O(min(n, m))
        if (n > m) {
            String tmpStr = s1;
            s1 = s2;
            s2 = tmpStr;
            int tmpLen = n;
            n = m;
            m = tmpLen;
        }

        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            prev[i] = i;
        }

        for (int j = 1; j <= m; j++) {
            char c2 = s2.charAt(start + j - 1);

            // Ограничиваем диапазон вычислений полосой шириной 2 * maxDist + 1 вокруг главной диагонали
            int from = Math.max(1, j - maxDist);
            int to = Math.min(n, j + maxDist);
            curr[0] = j;
            if (from > 1) {
                curr[from - 1] = maxDist + 1;
            }
            int minInRow = maxDist + 1;
            for (int i = from; i <= to; i++) {
                int cost = (s1.charAt(start + i - 1) == c2) ? 0 : 1;
                int val = Math.min(
                        Math.min(curr[i - 1] + 1, prev[i] + 1),
                        prev[i - 1] + cost
                );
                curr[i] = val;
                if (val < minInRow) {
                    minInRow = val;
                }
            }
            if (to < n) {
                curr[to + 1] = maxDist + 1;
            }
// Если минимальное значение в строке уже превысило порог, прерываем расчет
            if (minInRow > maxDist) {
                return maxDist + 1;
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        return prev[n];
    }
}