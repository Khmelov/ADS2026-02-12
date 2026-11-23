package by.it.group551001.bondarenko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

public class SourceScannerC {

    private static final int COPY_THRESHOLD = 10;

    private static class SourceFile {
        final String relativePath;
        final String normalizedText;

        SourceFile(String relativePath, String normalizedText) {
            this.relativePath = relativePath;
            this.normalizedText = normalizedText;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Path.of(src);

        List<SourceFile> files = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                String content = readIgnoringErrors(p);
                if (content == null || content.contains("@Test") || content.contains("org.junit.Test")) {
                    return;
                }

                String noComments = removeComments(content);
                String noPkgImports = stripPackageAndImports(noComments);
                String normalized = collapseWhitespace(noPkgImports);

                String relPath = root.relativize(p).toString();
                files.add(new SourceFile(relPath, normalized));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка по длине текста позволяет мгновенно отсекать пары с разницей длин >= 10
        files.sort(Comparator.comparingInt(f -> f.normalizedText.length()));

        Map<String, TreeSet<String>> copiesMap = new TreeMap<>();

        int count = files.size();
        for (int i = 0; i < count; i++) {
            SourceFile f1 = files.get(i);
            String s1 = f1.normalizedText;
            int len1 = s1.length();

            for (int j = i + 1; j < count; j++) {
                SourceFile f2 = files.get(j);
                String s2 = f2.normalizedText;
                int len2 = s2.length();

                if (len2 - len1 >= COPY_THRESHOLD) {
                    break;
                }

                if (levenshteinBounded(s1, s2, COPY_THRESHOLD) < COPY_THRESHOLD) {
                    copiesMap.computeIfAbsent(f1.relativePath, k -> new TreeSet<>()).add(f2.relativePath);
                    copiesMap.computeIfAbsent(f2.relativePath, k -> new TreeSet<>()).add(f1.relativePath);
                }
            }
        }

        for (Map.Entry<String, TreeSet<String>> entry : copiesMap.entrySet()) {
            System.out.println(entry.getKey());
            for (String copyPath : entry.getValue()) {
                System.out.println(copyPath);
            }
        }
    }

    /**
     * Вычисляет расстояние Левенштейна с ограничением maxDist.
     * Если расстояние >= maxDist, возвращает maxDist и досрочно завершает расчёт.
     */
    private static int levenshteinBounded(String s1, String s2, int maxDist) {
        int n = s1.length();
        int m = s2.length();

        if (Math.abs(n - m) >= maxDist) {
            return maxDist;
        }
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }

        if (n > m) {
            String tmpStr = s1;
            s1 = s2;
            s2 = tmpStr;
            int tmpLen = n;
            n = m;
            m = tmpLen;
        }

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            char c1 = s1.charAt(i - 1);

            int from = Math.max(1, i - maxDist);
            int to = Math.min(m, i + maxDist);

            if (from > 1) {
                curr[from - 1] = maxDist;
            }
            if (to < m) {
                Arrays.fill(curr, to + 1, m + 1, maxDist);
            }

            int rowMin = curr[0];
            for (int j = from; j <= to; j++) {
                int cost = (c1 == s2.charAt(j - 1)) ? 0 : 1;
                int val = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );
                curr[j] = val;
                if (val < rowMin) {
                    rowMin = val;
                }
            }

            if (rowMin >= maxDist) {
                return maxDist;
            }

            int[] swap = prev;
            prev = curr;
            curr = swap;
        }

        return prev[m];
    }

    private static String readIgnoringErrors(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.IGNORE)
                    .onUnmappableCharacter(CodingErrorAction.IGNORE);
            CharBuffer decoded = decoder.decode(ByteBuffer.wrap(bytes));
            return decoded.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private static String removeComments(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        int i = 0;

        while (i < n) {
            char c = text.charAt(i);

            if (c == '"') {
                sb.append(c);
                i++;
                while (i < n) {
                    char sc = text.charAt(i);
                    sb.append(sc);
                    if (sc == '\\' && i + 1 < n) {
                        i++;
                        sb.append(text.charAt(i));
                    } else if (sc == '"') {
                        i++;
                        break;
                    }
                    i++;
                }
            } else if (c == '\'') {
                sb.append(c);
                i++;
                while (i < n) {
                    char cc = text.charAt(i);
                    sb.append(cc);
                    if (cc == '\\' && i + 1 < n) {
                        i++;
                        sb.append(text.charAt(i));
                    } else if (cc == '\'') {
                        i++;
                        break;
                    }
                    i++;
                }
            } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '/') {
                i += 2;
                while (i < n && text.charAt(i) != '\n') {
                    i++;
                }
            } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '*') {
                i += 2;
                while (i < n) {
                    if (text.charAt(i) == '*' && i + 1 < n && text.charAt(i + 1) == '/') {
                        i += 2;
                        break;
                    }
                    i++;
                }
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    private static String stripPackageAndImports(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        int i = 0;

        while (i < n) {
            int lineStart = i;
            while (i < n && text.charAt(i) != '\n') {
                i++;
            }
            int lineEnd = i;
            if (i < n && text.charAt(i) == '\n') {
                i++;
            }

            int j = lineStart;
            while (j < lineEnd && text.charAt(j) <= ' ') {
                j++;
            }

            if (startsWithKeyword(text, j, lineEnd, "package") ||
                    startsWithKeyword(text, j, lineEnd, "import")) {
                continue;
            }

            sb.append(text, lineStart, i);
        }
        return sb.toString();
    }

    private static boolean startsWithKeyword(String text, int start, int end, String keyword) {
        int kLen = keyword.length();
        if (end - start < kLen) {
            return false;
        }
        if (!text.regionMatches(start, keyword, 0, kLen)) {
            return false;
        }
        return end - start == kLen || !Character.isJavaIdentifierPart(text.charAt(start + kLen));
    }

    private static String collapseWhitespace(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        boolean inSpace = false;

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
        return sb.toString().trim();
    }
}
