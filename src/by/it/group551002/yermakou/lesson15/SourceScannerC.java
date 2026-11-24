package by.it.group551002.yermakou.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SourceScannerC {

    private static final int MAX_EDITS = 9;

    private static final Charset[] CHARSETS = {
            StandardCharsets.UTF_8,
            Charset.forName("windows-1251"),
            StandardCharsets.ISO_8859_1
    };

    private static final class Doc {
        final String path;
        final char[] text;

        Doc(String path, char[] text) {
            this.path = path;
            this.text = text;
        }
    }

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Paths.get(src);
        if (!Files.isDirectory(root)) {
            return;
        }

        List<Path> files;
        try (Stream<Path> walk = Files.walk(root)) {
            files = walk.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .toList();
        }

        List<Doc> docs = new ArrayList<>();
        for (Path p : files) {
            byte[] bytes;
            try {
                bytes = Files.readAllBytes(p);
            } catch (IOException e) {
                continue;
            }
            String text = decode(bytes);
            if (text.contains("@" + "Test") || text.contains("org.junit." + "Test")) {
                continue;
            }
            text = stripComments(text);
            text = stripPackageAndImports(text);
            text = collapseWhitespace(text);
            docs.add(new Doc(root.relativize(p).toString(), text.toCharArray()));
        }

        Doc[] arr = docs.toArray(new Doc[0]);
        Arrays.sort(arr, Comparator.comparingInt(d -> d.text.length));
        int n = arr.length;

        List<int[]> pairs = IntStream.range(0, n).parallel().boxed()
                .flatMap(i -> {
                    List<int[]> local = new ArrayList<>();
                    char[] a = arr[i].text;
                    for (int j = i + 1; j < n; j++) {
                        char[] b = arr[j].text;
                        if (b.length - a.length > MAX_EDITS) {
                            break;
                        }
                        if (within(a, b, MAX_EDITS)) {
                            local.add(new int[]{i, j});
                        }
                    }
                    return local.stream();
                })
                .toList();

        Map<String, TreeSet<String>> copies = new TreeMap<>();
        for (int[] pr : pairs) {
            String p1 = arr[pr[0]].path;
            String p2 = arr[pr[1]].path;
            copies.computeIfAbsent(p1, k -> new TreeSet<>()).add(p2);
            copies.computeIfAbsent(p2, k -> new TreeSet<>()).add(p1);
        }

        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, TreeSet<String>> e : copies.entrySet()) {
            out.append(e.getKey()).append('\n');
            for (String c : e.getValue()) {
                out.append(c).append('\n');
            }
        }
        System.out.print(out);
    }


    private static boolean within(char[] a, char[] b, int k) {
        int n = a.length;
        int m = b.length;
        if (Math.abs(n - m) > k) {
            return false;
        }

        int p = 0;
        int minLen = Math.min(n, m);
        while (p < minLen && a[p] == b[p]) {
            p++;
        }
        int s = 0;
        while (s < minLen - p && a[n - 1 - s] == b[m - 1 - s]) {
            s++;
        }
        int la = n - p - s;
        int lb = m - p - s;
        if (la == 0 || lb == 0) {
            return Math.max(la, lb) <= k;
        }

        final int inf = k + 1;
        int[] prev = new int[lb + 1];
        int[] cur = new int[lb + 1];
        for (int j = 0; j <= lb; j++) {
            prev[j] = j <= k ? j : inf;
        }
        for (int i = 1; i <= la; i++) {
            int lo = Math.max(1, i - k);
            int hi = Math.min(lb, i + k);
            cur[lo - 1] = (lo == 1) ? Math.min(i, inf) : inf;
            if (hi < lb) {
                cur[hi + 1] = inf;
            }
            char ca = a[p + i - 1];
            int rowMin = inf;
            for (int j = lo; j <= hi; j++) {
                int v = prev[j - 1] + (ca == b[p + j - 1] ? 0 : 1);
                int del = prev[j] + 1;
                int ins = cur[j - 1] + 1;
                if (del < v) {
                    v = del;
                }
                if (ins < v) {
                    v = ins;
                }
                if (v > inf) {
                    v = inf;
                }
                cur[j] = v;
                if (v < rowMin) {
                    rowMin = v;
                }
            }
            if (rowMin > k) {
                return false;
            }
            int[] t = prev;
            prev = cur;
            cur = t;
        }
        return prev[lb] <= k;
    }

    private static String decode(byte[] bytes) {
        for (Charset cs : CHARSETS) {
            try {
                return cs.newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT)
                        .decode(ByteBuffer.wrap(bytes))
                        .toString();
            } catch (CharacterCodingException e) {

            }
        }
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }

    private static String stripComments(String t) {
        int n = t.length();
        StringBuilder sb = new StringBuilder(n);
        int i = 0;
        while (i < n) {
            char c = t.charAt(i);
            if (c == '/' && i + 1 < n && t.charAt(i + 1) == '/') {
                i += 2;
                while (i < n && t.charAt(i) != '\n') {
                    i++;
                }
            } else if (c == '/' && i + 1 < n && t.charAt(i + 1) == '*') {
                i += 2;
                while (i < n && !(t.charAt(i) == '*' && i + 1 < n && t.charAt(i + 1) == '/')) {
                    i++;
                }
                i = Math.min(n, i + 2);
            } else if (c == '"' && t.startsWith("\"\"\"", i)) {
                int j = i + 3;
                while (j < n && !t.startsWith("\"\"\"", j)) {
                    if (t.charAt(j) == '\\') {
                        j++;
                    }
                    j++;
                }
                j = Math.min(n, j + 3);
                sb.append(t, i, j);
                i = j;
            } else if (c == '"' || c == '\'') {
                int j = i + 1;
                while (j < n && t.charAt(j) != c && t.charAt(j) != '\n') {
                    if (t.charAt(j) == '\\') {
                        j++;
                    }
                    j++;
                }
                j = Math.min(n, j + 1);
                sb.append(t, i, j);
                i = j;
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
        int pos = 0;
        while (pos < n) {
            int end = text.indexOf('\n', pos);
            int next = (end == -1) ? n : end + 1;
            int i = pos;
            while (i < next && text.charAt(i) <= ' ') {
                i++;
            }
            if (!startsWithWord(text, i, next, "package")
                    && !startsWithWord(text, i, next, "import")) {
                sb.append(text, pos, next);
            }
            pos = next;
        }
        return sb.toString();
    }

    private static String collapseWhitespace(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n);
        boolean inSpace = false;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c < 33) {
                inSpace = true;
            } else {
                if (inSpace) {
                    sb.append(' ');
                    inSpace = false;
                }
                sb.append(c);
            }
        }
        if (inSpace) {
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    private static boolean startsWithWord(String s, int from, int limit, String word) {
        int len = word.length();
        return from + len < limit
                && s.startsWith(word, from)
                && s.charAt(from + len) <= ' ';
    }
}