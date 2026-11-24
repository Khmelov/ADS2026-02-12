package by.it.group551002.savitsky.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerC {

    private static final int MAX_EDITS = 9; // копия: число правок < 10

    private static class Doc {
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

        List<Path> files;
        try (Stream<Path> walk = Files.walk(root)) {
            files = walk
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }

        List<Doc> docs = new ArrayList<>();
        for (Path p : files) {
            String text = readText(p);

            if (text.contains("@Test") || text.contains("org.junit.Test")) {
                continue;
            }

            text = removeComments(text);
            text = removePackageAndImports(text);
            text = collapseWhitespace(text);
            text = text.trim();

            docs.add(new Doc(root.relativize(p).toString(), text.toCharArray()));
        }

        docs.sort(Comparator.comparingInt(d -> d.text.length));

        int k = docs.size();
        List<TreeSet<String>> copies = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            copies.add(new TreeSet<>());
        }

        for (int i = 0; i < k; i++) {
            char[] a = docs.get(i).text;
            for (int j = i + 1; j < k; j++) {
                char[] b = docs.get(j).text;
                if (b.length - a.length > MAX_EDITS) {
                    break; // дальше длины только растут
                }
                if (distanceWithin(a, b, MAX_EDITS) <= MAX_EDITS) {
                    copies.get(i).add(docs.get(j).path);
                    copies.get(j).add(docs.get(i).path);
                }
            }
        }

        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            if (!copies.get(i).isEmpty()) {
                order.add(i);
            }
        }
        order.sort(Comparator.comparing(i -> docs.get(i).path));

        for (int i : order) {
            System.out.println(docs.get(i).path);
            for (String copy : copies.get(i)) {
                System.out.println(copy);
            }
        }
    }

    private static String readText(Path p) throws IOException {
        try {
            return Files.readString(p);
        } catch (MalformedInputException e) {
            return new String(Files.readAllBytes(p), "windows-1251");
        }
    }

    private static String removeComments(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        int i = 0;

        while (i < n) {
            char c = text.charAt(i);

            if (c == '/' && i + 1 < n && text.charAt(i + 1) == '/') {
                i += 2;
                while (i < n && text.charAt(i) != '\n') {
                    i++;
                }
            } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < n && !(text.charAt(i) == '*' && text.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2;
                sb.append(' ');
            } else if (c == '"' || c == '\'') {
                sb.append(c);
                i++;
                while (i < n) {
                    char ch = text.charAt(i);
                    sb.append(ch);
                    i++;
                    if (ch == '\\' && i < n) {
                        sb.append(text.charAt(i));
                        i++;
                    } else if (ch == c) {
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

    private static String removePackageAndImports(String text) {
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

    private static boolean startsWithWord(String text, int i, int limit, String word) {
        int len = word.length();
        return i + len < limit
                && text.startsWith(word, i)
                && text.charAt(i + len) <= ' ';
    }

    private static String collapseWhitespace(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        boolean space = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                space = true;
            } else {
                if (space) {
                    sb.append(' ');
                    space = false;
                }
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static int distanceWithin(char[] a, char[] b, int max) {
        int n = a.length, m = b.length;
        int inf = max + 1;
        if (Math.abs(n - m) > max) {
            return inf;
        }

        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        for (int j = 0; j <= m; j++) {
            prev[j] = (j <= max) ? j : inf;
        }

        for (int i = 1; i <= n; i++) {
            int from = Math.max(1, i - max);
            int to = Math.min(m, i + max);

            cur[from - 1] = (from == 1) ? i : inf;
            int rowMin = cur[from - 1];

            for (int j = from; j <= to; j++) {
                int cost = (a[i - 1] == b[j - 1]) ? 0 : 1;
                int v = Math.min(prev[j - 1] + cost,
                        Math.min(prev[j] + 1, cur[j - 1] + 1));
                cur[j] = v;
                if (v < rowMin) {
                    rowMin = v;
                }
            }
            if (to < m) {
                cur[to + 1] = inf; // граница полосы для следующей строки
            }

            if (rowMin > max) {
                return inf; // ранний выход
            }
            int[] t = prev; prev = cur; cur = t;
        }
        return Math.min(prev[m], inf);
    }
}