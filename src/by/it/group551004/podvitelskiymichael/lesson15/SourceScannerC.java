package by.it.group551004.podvitelskiymichael.lesson15;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SourceScannerC {

    private static final int MAX_EDITS = 9;

    private static final boolean SAME_NAME_ONLY = false;

    private static final Charset CP1251 = Charset.forName("windows-1251");

    private record Sample(String path, String name, String text) {
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Path.of(src);

        List<Sample> samples;
        try (Stream<Path> walk = Files.walk(root)) {
            List<Path> files = walk
                    .filter(p -> p.toString().endsWith(".java"))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            samples = files.parallelStream()
                    .map(p -> load(root, p))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException | UncheckedIOException e) {
            return;
        }

        int n = samples.size();
        Sample[] byLen = samples.toArray(new Sample[0]);
        Arrays.sort(byLen, Comparator.comparingInt(s -> s.text().length()));

        @SuppressWarnings("unchecked")
        List<Integer>[] forward = new List[n];
        IntStream.range(0, n).parallel().forEach(i -> {
            List<Integer> found = null;
            String a = byLen[i].text();
            for (int j = i + 1; j < n; j++) {
                String b = byLen[j].text();
                if (b.length() - a.length() > MAX_EDITS) {
                    break;
                }
                if (SAME_NAME_ONLY && !byLen[i].name().equals(byLen[j].name())) {
                    continue;
                }
                if (distanceAtMost(a, b, MAX_EDITS) <= MAX_EDITS) {
                    if (found == null) {
                        found = new ArrayList<>();
                    }
                    found.add(j);
                }
            }
            forward[i] = found;
        });

        Map<String, TreeSet<String>> copies = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            if (forward[i] == null) {
                continue;
            }
            for (int j : forward[i]) {
                copies.computeIfAbsent(byLen[i].path(), k -> new TreeSet<>()).add(byLen[j].path());
                copies.computeIfAbsent(byLen[j].path(), k -> new TreeSet<>()).add(byLen[i].path());
            }
        }

        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, TreeSet<String>> e : copies.entrySet()) {
            out.append(e.getKey()).append('\n');
            for (String copy : e.getValue()) {
                out.append(copy).append('\n');
            }
        }
        System.out.print(out);
        System.out.flush();
    }

    private static Sample load(Path root, Path p) {
        try {
            String raw = decode(Files.readAllBytes(p));
            if (raw.contains("@Test") || raw.contains("org.junit.Test")) {
                return null;
            }
            String text = normalize(stripComments(removeHeader(raw)));
            return new Sample(root.relativize(p).toString(), p.getFileName().toString(), text);
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private static String decode(byte[] bytes) {
        String s;
        try {
            s = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes))
                    .toString();
        } catch (CharacterCodingException e) {
            s = new String(bytes, CP1251);
        }
        if (!s.isEmpty() && s.charAt(0) == '\uFEFF') {
            s = s.substring(1);
        }
        return s;
    }

    private static String removeHeader(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n);
        int lineStart = 0;
        while (lineStart < n) {
            int lineEnd = s.indexOf('\n', lineStart);
            if (lineEnd < 0) {
                lineEnd = n;
            }
            int k = lineStart;
            while (k < lineEnd && s.charAt(k) <= ' ') {
                k++;
            }
            if (!isKeyword(s, k, lineEnd, "package") && !isKeyword(s, k, lineEnd, "import")) {
                sb.append(s, lineStart, Math.min(lineEnd + 1, n));
            }
            lineStart = lineEnd + 1;
        }
        return sb.toString();
    }

    private static boolean isKeyword(String s, int from, int end, String kw) {
        int len = kw.length();
        return from + len < end && s.startsWith(kw, from) && s.charAt(from + len) <= ' ';
    }

    private static String stripComments(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n);
        int i = 0;
        while (i < n) {
            char c = s.charAt(i);
            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '/') {
                i += 2;
                while (i < n && s.charAt(i) != '\n') {
                    i++;
                }
            } else if (c == '/' && i + 1 < n && s.charAt(i + 1) == '*') {
                i += 2;
                while (i < n && !(s.charAt(i) == '*' && i + 1 < n && s.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2;
                sb.append(' ');
            } else if (c == '"' && s.startsWith("\"\"\"", i)) {
                sb.append("\"\"\"");
                i += 3;
                while (i < n && !s.startsWith("\"\"\"", i)) {
                    if (s.charAt(i) == '\\' && i + 1 < n) {
                        sb.append(s.charAt(i++));
                    }
                    sb.append(s.charAt(i++));
                }
                if (i < n) {
                    sb.append("\"\"\"");
                    i += 3;
                }
            } else if (c == '"' || c == '\'') {
                sb.append(c);
                i++;
                while (i < n) {
                    char d = s.charAt(i);
                    if (d == '\\' && i + 1 < n) {
                        sb.append(d).append(s.charAt(i + 1));
                        i += 2;
                        continue;
                    }
                    if (d == '\n') {
                        break;
                    }
                    sb.append(d);
                    i++;
                    if (d == c) {
                        break;
                    }
                }
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    private static String normalize(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n);
        boolean space = false;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c < 33) {
                if (!space) {
                    sb.append(' ');
                    space = true;
                }
            } else {
                sb.append(c);
                space = false;
            }
        }
        return sb.toString().trim();
    }

    private static int distanceAtMost(String a, String b, int max) {
        int la = a.length(), lb = b.length();
        if (Math.abs(la - lb) > max) {
            return max + 1;
        }
        int start = 0;
        int min = Math.min(la, lb);
        while (start < min && a.charAt(start) == b.charAt(start)) {
            start++;
        }
        int ea = la, eb = lb;
        while (ea > start && eb > start && a.charAt(ea - 1) == b.charAt(eb - 1)) {
            ea--;
            eb--;
        }
        if (ea - start < eb - start) {
            String t = a; a = b; b = t;
            int te = ea; ea = eb; eb = te;
        }
        int n = ea - start;
        int m = eb - start;
        if (m == 0) {
            return Math.min(n, max + 1);
        }

        final int inf = max + 1;
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        for (int j = 0; j <= m; j++) {
            prev[j] = Math.min(j, inf);
        }
        for (int i = 1; i <= n; i++) {
            int lo = Math.max(1, i - max);
            int hi = Math.min(m, i + max);
            cur[lo - 1] = (lo == 1) ? Math.min(i, inf) : inf;
            if (hi + 1 <= m) {
                cur[hi + 1] = inf;
            }
            char ca = a.charAt(start + i - 1);
            int rowMin = inf;
            for (int j = lo; j <= hi; j++) {
                int v = prev[j - 1] + (ca == b.charAt(start + j - 1) ? 0 : 1);
                int up = prev[j] + 1;
                int left = cur[j - 1] + 1;
                if (up < v) v = up;
                if (left < v) v = left;
                if (v > inf) v = inf;
                cur[j] = v;
                if (v < rowMin) rowMin = v;
            }
            if (rowMin > max) {
                return inf;
            }
            int[] t = prev; prev = cur; cur = t;
        }
        return Math.min(prev[m], inf);
    }
}