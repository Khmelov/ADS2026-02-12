package by.it.group510902.sorokoletov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerC {

    private static final int MAX_EDITS = 10;

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path srcPath = Paths.get(src);
        List<TextInfo> texts = new ArrayList<>();

        List<Path> javaFiles;
        try (Stream<Path> stream = Files.walk(srcPath)) {
            javaFiles = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }

        for (Path file : javaFiles) {
            String content;
            try {
                byte[] bytes = Files.readAllBytes(file);
                content = new String(bytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                continue;
            }

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                continue;
            }

            String processed = removePackageAndImports(content);
            processed = removeComments(processed);
            processed = normalizeWhitespace(processed);
            processed = processed.trim();

            String relPath = srcPath.relativize(file).toString();
            texts.add(new TextInfo(relPath, processed, buildHistogram(processed)));
        }

        texts.sort((a, b) -> a.path.compareTo(b.path));

        int n = texts.size();
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        Map<Integer, List<Integer>> byLen = new HashMap<>();
        for (int i = 0; i < n; i++) {
            byLen.computeIfAbsent(texts.get(i).text.length(), k -> new ArrayList<>()).add(i);
        }

        for (int i = 0; i < n; i++) {
            TextInfo ti = texts.get(i);
            int lenI = ti.text.length();

            for (int delta = -MAX_EDITS + 1; delta <= MAX_EDITS - 1; delta++) {
                List<Integer> candidates = byLen.get(lenI + delta);
                if (candidates == null) continue;

                for (int j : candidates) {
                    if (j <= i) continue;
                    if (find(parent, i) == find(parent, j)) continue;

                    TextInfo tj = texts.get(j);

                    if (histogramDistance(ti.hist, tj.hist) >= MAX_EDITS) continue;

                    int d = boundedLevenshteinFast(ti.text, tj.text, MAX_EDITS);
                    if (d < MAX_EDITS) {
                        union(parent, i, j);
                    }
                }
            }
        }

        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find(parent, i);
            groups.computeIfAbsent(root, k -> new ArrayList<>()).add(i);
        }

        List<List<Integer>> resultGroups = new ArrayList<>();
        for (List<Integer> group : groups.values()) {
            if (group.size() > 1) {
                Collections.sort(group, (a, b) -> texts.get(a).path.compareTo(texts.get(b).path));
                resultGroups.add(group);
            }
        }

        resultGroups.sort((g1, g2) ->
                texts.get(g1.get(0)).path.compareTo(texts.get(g2.get(0)).path));

        for (List<Integer> group : resultGroups) {
            for (int idx : group) {
                System.out.println(texts.get(idx).path);
            }
        }
    }

    private static int[] buildHistogram(String s) {
        int[] h = new int[65536];
        for (int i = 0; i < s.length(); i++) {
            h[s.charAt(i)]++;
        }
        return h;
    }

    private static int histogramDistance(int[] a, int[] b) {
        int diff = 0;
        for (int i = 0; i < a.length; i++) {
            diff += Math.abs(a[i] - b[i]);
            if (diff >= MAX_EDITS * 2) return diff;
        }
        return diff;
    }

    private static int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    private static void union(int[] parent, int a, int b) {
        int ra = find(parent, a);
        int rb = find(parent, b);
        if (ra != rb) parent[rb] = ra;
    }

    private static int boundedLevenshteinFast(String a, String b, int maxDist) {
        int la = a.length();
        int lb = b.length();

        if (Math.abs(la - lb) >= maxDist) return maxDist;

        int prefix = 0;
        int minLen = Math.min(la, lb);
        while (prefix < minLen && a.charAt(prefix) == b.charAt(prefix)) prefix++;

        int suffix = 0;
        while (suffix < minLen - prefix
                && a.charAt(la - 1 - suffix) == b.charAt(lb - 1 - suffix)) suffix++;

        int aStart = prefix;
        int aEnd = la - suffix;
        int bStart = prefix;
        int bEnd = lb - suffix;

        int aLen = aEnd - aStart;
        int bLen = bEnd - bStart;

        if (aLen == 0) return bLen < maxDist ? bLen : maxDist;
        if (bLen == 0) return aLen < maxDist ? aLen : maxDist;

        if (Math.abs(aLen - bLen) >= maxDist) return maxDist;

        int[] prev = new int[bLen + 1];
        int[] curr = new int[bLen + 1];

        for (int j = 0; j <= bLen; j++) prev[j] = j;

        for (int i = 1; i <= aLen; i++) {
            curr[0] = i;
            int rowMin = curr[0];
            char ca = a.charAt(aStart + i - 1);

            for (int j = 1; j <= bLen; j++) {
                int cost = (ca == b.charAt(bStart + j - 1)) ? 0 : 1;
                int v = Math.min(
                        Math.min(prev[j] + 1, curr[j - 1] + 1),
                        prev[j - 1] + cost
                );
                curr[j] = v;
                if (v < rowMin) rowMin = v;
            }

            if (rowMin >= maxDist) return maxDist;

            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return prev[bLen];
    }

    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int i = 0;
        int n = text.length();

        while (i < n) {
            int lineEnd = text.indexOf('\n', i);
            if (lineEnd == -1) lineEnd = n;

            String line = text.substring(i, lineEnd);
            String trimmed = line.trim();

            boolean skip = trimmed.startsWith("package")
                    || trimmed.startsWith("import");

            if (!skip) {
                sb.append(line);
                if (lineEnd < n) sb.append('\n');
            }

            i = lineEnd + 1;
        }

        return sb.toString();
    }

    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;

        while (i < n) {
            char c = text.charAt(i);

            if (c == '/' && i + 1 < n) {
                char next = text.charAt(i + 1);

                if (next == '/') {
                    int end = text.indexOf('\n', i);
                    if (end == -1) end = n;
                    i = end;
                    continue;
                }

                if (next == '*') {
                    int end = text.indexOf("*/", i + 2);
                    if (end == -1) {
                        i = n;
                    } else {
                        i = end + 2;
                    }
                    continue;
                }
            }

            if (c == '"') {
                sb.append(c);
                i++;
                while (i < n) {
                    char sc = text.charAt(i);
                    sb.append(sc);
                    if (sc == '\\' && i + 1 < n) {
                        sb.append(text.charAt(i + 1));
                        i += 2;
                        continue;
                    }
                    i++;
                    if (sc == '"') break;
                }
                continue;
            }

            if (c == '\'') {
                sb.append(c);
                i++;
                while (i < n) {
                    char sc = text.charAt(i);
                    sb.append(sc);
                    if (sc == '\\' && i + 1 < n) {
                        sb.append(text.charAt(i + 1));
                        i += 2;
                        continue;
                    }
                    i++;
                    if (sc == '\'') break;
                }
                continue;
            }

            sb.append(c);
            i++;
        }

        return sb.toString();
    }

    private static String normalizeWhitespace(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;
        boolean inSpace = false;

        while (i < n) {
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
            i++;
        }

        return sb.toString();
    }

    private static class TextInfo {
        final String path;
        final String text;
        final int[] hist;

        TextInfo(String path, String text, int[] hist) {
            this.path = path;
            this.text = text;
            this.hist = hist;
        }
    }
}