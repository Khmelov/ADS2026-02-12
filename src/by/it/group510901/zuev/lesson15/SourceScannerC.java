package by.it.group510901.zuev.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerC {

    private static final int BOUND = 10;

    public static void main(String[] args) throws IOException {
        String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcDir);

        List<FileEntry> entries;
        try (Stream<Path> walk = Files.walk(srcPath)) {
            entries = walk
                    .filter(p -> p.toString().endsWith(".java"))
                    .filter(Files::isRegularFile)
                    .parallel()
                    .map(p -> processFile(srcPath, p))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        // 1. Группируем полностью идентичные тексты — для них расстояние = 0 без Левенштейна.
        Map<String, List<String>> byText = new HashMap<>(entries.size());
        for (FileEntry e : entries) {
            byText.computeIfAbsent(e.text, k -> new ArrayList<>()).add(e.path);
        }

        List<TextGroup> groups = new ArrayList<>(byText.size());
        for (Map.Entry<String, List<String>> e : byText.entrySet()) {
            groups.add(new TextGroup(e.getKey(), e.getValue()));
        }
        groups.sort(Comparator.comparingInt(g -> g.text.length()));

        TreeMap<String, List<String>> copies = new TreeMap<>();

        // 2. Копии внутри одной группы (точные совпадения).
        for (TextGroup g : groups) {
            if (g.paths.size() > 1) {
                for (String p1 : g.paths) {
                    List<String> list = copies.computeIfAbsent(p1, k -> new ArrayList<>());
                    for (String p2 : g.paths) {
                        if (!p1.equals(p2)) list.add(p2);
                    }
                }
            }
        }

        // 3. Попарное сравнение только уникальных текстов.
        int[] prevBuf = new int[0];
        int[] currBuf = new int[0];

        int n = groups.size();
        for (int i = 0; i < n; i++) {
            TextGroup a = groups.get(i);
            int la = a.text.length();

            for (int j = i + 1; j < n; j++) {
                TextGroup b = groups.get(j);
                if (b.text.length() - la >= BOUND) break;

                if (!mayBeClose(a.hist, b.hist, BOUND)) continue;

                int need = Math.min(la, b.text.length()) + 1;
                if (prevBuf.length < need) {
                    prevBuf = new int[need];
                    currBuf = new int[need];
                }

                int dist = levenshteinBounded(a.text, b.text, BOUND, prevBuf, currBuf);
                if (dist < BOUND) {
                    for (String pa : a.paths) {
                        copies.computeIfAbsent(pa, k -> new ArrayList<>()).addAll(b.paths);
                    }
                    for (String pb : b.paths) {
                        copies.computeIfAbsent(pb, k -> new ArrayList<>()).addAll(a.paths);
                    }
                }
            }
        }

        // 4. Буферизованный вывод.
        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : copies.entrySet()) {
            out.append(entry.getKey()).append('\n');
            List<String> sorted = entry.getValue();
            sorted.sort(String::compareTo);
            for (String copy : sorted) {
                out.append(copy).append('\n');
            }
        }
        System.out.print(out);
    }

    // ---------- Обработка одного файла ----------

    private static FileEntry processFile(Path srcPath, Path file) {
        String content;
        try {
            // readAllBytes + new String(...) не падает с MalformedInputException
            content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }

        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return null;
        }

        if (!content.isEmpty() && content.charAt(0) == '\uFEFF') {
            content = content.substring(1);
        }

        // Сначала комментарии, затем package/import, затем схлопывание control-символов.
        String noComments = removeComments(content);
        String withoutHeader = removePackageAndImports(noComments);
        String cleaned = collapseControlChars(withoutHeader);

        String relative = srcPath.relativize(file).toString();
        return new FileEntry(relative, cleaned);
    }

    // ---------- Удаление package / import ----------

    private static String removePackageAndImports(String content) {
        StringBuilder sb = new StringBuilder(content.length());
        int start = 0;
        int n = content.length();
        for (int i = 0; i <= n; i++) {
            if (i == n || content.charAt(i) == '\n') {
                int lineStart = start;
                int lineEnd = i;
                int trimmedEnd = lineEnd;
                if (trimmedEnd > lineStart && content.charAt(trimmedEnd - 1) == '\r') {
                    trimmedEnd--;
                }
                int k = lineStart;
                while (k < trimmedEnd && (content.charAt(k) == ' ' || content.charAt(k) == '\t')) k++;

                boolean drop = false;
                if (startsWith(content, k, trimmedEnd, "package ")
                        || startsWith(content, k, trimmedEnd, "import ")) {
                    int last = trimmedEnd - 1;
                    while (last >= k && (content.charAt(last) == ' ' || content.charAt(last) == '\t')) last--;
                    if (last >= k && content.charAt(last) == ';') drop = true;
                }

                if (!drop) {
                    sb.append(content, lineStart, lineEnd);
                    sb.append('\n');
                }
                start = i + 1;
            }
        }
        return sb.toString();
    }

    private static boolean startsWith(String s, int from, int to, String prefix) {
        if (to - from < prefix.length()) return false;
        for (int i = 0; i < prefix.length(); i++) {
            if (s.charAt(from + i) != prefix.charAt(i)) return false;
        }
        return true;
    }

    // ---------- Удаление комментариев за O(n) ----------

    static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;
        int lastCopied = 0;

        boolean inString = false;
        boolean inChar = false;
        boolean inTextBlock = false;

        while (i < n) {
            char c = text.charAt(i);

            if (inTextBlock) {
                if (c == '"' && i + 2 < n
                        && text.charAt(i + 1) == '"' && text.charAt(i + 2) == '"') {
                    inTextBlock = false;
                    i += 3;
                } else if (c == '\\' && i + 1 < n) {
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            if (inString) {
                if (c == '\\' && i + 1 < n) {
                    i += 2;
                } else if (c == '"') {
                    inString = false;
                    i++;
                } else {
                    i++;
                }
                continue;
            }

            if (inChar) {
                if (c == '\\' && i + 1 < n) {
                    i += 2;
                } else if (c == '\'') {
                    inChar = false;
                    i++;
                } else {
                    i++;
                }
                continue;
            }

            if (c == '/' && i + 1 < n) {
                char c2 = text.charAt(i + 1);
                if (c2 == '/') {
                    sb.append(text, lastCopied, i);
                    int j = i + 2;
                    while (j < n && text.charAt(j) != '\n') j++;
                    i = j;
                    lastCopied = i;
                    continue;
                }
                if (c2 == '*') {
                    sb.append(text, lastCopied, i);
                    int j = i + 2;
                    while (j + 1 < n && !(text.charAt(j) == '*' && text.charAt(j + 1) == '/')) j++;
                    i = (j + 1 < n) ? j + 2 : n;
                    lastCopied = i;
                    continue;
                }
            }

            if (c == '"') {
                if (i + 2 < n && text.charAt(i + 1) == '"' && text.charAt(i + 2) == '"') {
                    inTextBlock = true;
                    i += 3;
                    continue;
                }
                inString = true;
                i++;
                continue;
            }

            if (c == '\'') {
                inChar = true;
                i++;
                continue;
            }

            i++;
        }

        sb.append(text, lastCopied, n);
        return sb.toString();
    }

    // ---------- Схлопывание control-символов ----------

    private static String collapseControlChars(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        boolean wasControl = false;
        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!wasControl) {
                    sb.append(' ');
                    wasControl = true;
                }
            } else {
                sb.append(c);
                wasControl = false;
            }
        }
        int start = 0;
        int end = sb.length();
        while (start < end && sb.charAt(start) <= ' ') start++;
        while (end > start && sb.charAt(end - 1) <= ' ') end--;
        return sb.substring(start, end);
    }

    // ---------- Гистограмма символов ----------

    private static int[] histogram(String s) {
        int[] h = new int[257];
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c < 256) h[c]++;
            else h[256]++;
        }
        return h;
    }

    /**
     * Точная нижняя граница Левенштейна: max(sum(max(0, ha - hb)), sum(max(0, hb - ha))).
     */
    private static boolean mayBeClose(int[] ha, int[] hb, int bound) {
        int pos = 0;
        int neg = 0;
        for (int i = 0; i < ha.length; i++) {
            int d = ha[i] - hb[i];
            if (d > 0) {
                pos += d;
                if (pos >= bound) return false;
            } else if (d < 0) {
                neg -= d;
                if (neg >= bound) return false;
            }
        }
        return true;
    }

    // ---------- Полосовой Левенштейн (Ukkonen) O(L * BOUND) ----------

    static int levenshteinBounded(String a, String b, int bound, int[] prev, int[] curr) {
        int la = a.length(), lb = b.length();
        if (Math.abs(la - lb) >= bound) return bound;

        int p = 0;
        while (p < la && p < lb && a.charAt(p) == b.charAt(p)) p++;

        int s = 0;
        while (s < la - p && s < lb - p
                && a.charAt(la - 1 - s) == b.charAt(lb - 1 - s)) s++;

        int aStart = p, aLen = la - s - p;
        int bStart = p, bLen = lb - s - p;

        if (aLen == 0) return bLen;
        if (bLen == 0) return aLen;
        if (Math.abs(aLen - bLen) >= bound) return bound;

        if (bLen > aLen) {
            int t = aLen; aLen = bLen; bLen = t;
            int ts = aStart; aStart = bStart; bStart = ts;
            String tmp = a; a = b; b = tmp;
        }

        int maxDist = bound - 1;
        int initLimit = Math.min(bLen, maxDist);
        for (int j = 0; j <= initLimit; j++) prev[j] = j;

        for (int i = 1; i <= aLen; i++) {
            int from = Math.max(1, i - maxDist);
            int to = Math.min(bLen, i + maxDist);

            if (from == 1) curr[0] = i;
            else curr[from - 1] = bound;

            if (i + maxDist <= bLen) prev[to] = bound;

            int minInRow = bound;
            char ca = a.charAt(aStart + i - 1);
            for (int j = from; j <= to; j++) {
                int cost = (ca == b.charAt(bStart + j - 1)) ? 0 : 1;
                int v = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                curr[j] = v;
                if (v < minInRow) minInRow = v;
            }
            if (minInRow >= bound) return bound;

            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[bLen];
    }

    // ---------- DTO ----------

    private static class FileEntry {
        final String path;
        final String text;

        FileEntry(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }

    private static class TextGroup {
        final String text;
        final List<String> paths;
        final int[] hist;

        TextGroup(String text, List<String> paths) {
            this.text = text;
            this.paths = paths;
            this.hist = histogram(text);
        }
    }
}