
package by.it.group551004.sheev.lesson15;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC {

    static class Entry {
        String path;
        String text;
        int size;

        Entry(String path, String text) {
            this.path = path;
            this.text = text;
            this.size = text.length();
        }
    }

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path srcPath = Path.of(src);
        List<Entry> entries = new ArrayList<>();

        if (Files.exists(srcPath)) {
            List<Path> files = new ArrayList<>();

            try (Stream<Path> stream = Files.walk(srcPath)) {
                stream.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".java"))
                        .forEach(files::add);
            }

            for (Path p : files) {
                Entry e = readOne(p, srcPath);
                if (e != null) {
                    entries.add(e);
                }
            }
        }

        int n = entries.size();
        final int LIMIT = 10;

        List<List<Integer>> copies = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            copies.add(new ArrayList<>());
        }

        Integer[] order = new Integer[n];

        for (int i = 0; i < n; i++) {
            order[i] = i;
        }

        Arrays.sort(order, (a, b) ->
                Integer.compare(entries.get(a).size,
                        entries.get(b).size));

        for (int oi = 0; oi < n; oi++) {
            int i = order[oi];
            Entry a = entries.get(i);

            int oj = oi + 1;
            boolean stop = false;

            while (oj < n && !stop) {
                int j = order[oj];
                Entry b = entries.get(j);

                if (b.size - a.size >= LIMIT) {
                    stop = true;
                } else {
                    int distance = levenshtein(
                            a.text, b.text, LIMIT
                    );

                    if (distance < LIMIT) {
                        copies.get(i).add(j);
                        copies.get(j).add(i);
                    }

                    oj++;
                }
            }
        }

        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!copies.get(i).isEmpty()) {
                result.add(i);
            }
        }

        result.sort((a, b) ->
                entries.get(a).path.compareTo(entries.get(b).path));

        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(System.out, StandardCharsets.UTF_8),
                1 << 16
        );

        for (int idx : result) {
            bw.write(entries.get(idx).path);
            bw.newLine();

            List<Integer> current = copies.get(idx);

            current.sort((a, b) ->
                    entries.get(a).path.compareTo(entries.get(b).path));

            for (int c : current) {
                bw.write(entries.get(c).path);
                bw.newLine();
            }
        }

        bw.flush();
    }

    static Entry readOne(Path p, Path srcPath) {
        Entry result = null;

        try {
            byte[] bytes = Files.readAllBytes(p);

            String text = new String(bytes, StandardCharsets.UTF_8);

            boolean isTest = text.contains("@Test")
                    || text.contains("org.junit.Test");

            if (!isTest) {
                String processed = process(text);

                result = new Entry(
                        srcPath.relativize(p).toString(),
                        processed
                );
            }

        } catch (IOException e) {
            return null;
        }

        return result;
    }

    static String process(String text) {
        int n = text.length();

        StringBuilder sb = new StringBuilder(n);

        int i = 0;
        int state = 0;

        while (i < n) {
            int lineEnd = text.indexOf('\n', i);

            if (lineEnd < 0) {
                lineEnd = n;
            }

            int j = i;
            boolean skipLine = false;

            if (state == 0) {
                while (j < lineEnd &&
                        (text.charAt(j) == ' ' ||
                                text.charAt(j) == '\t' ||
                                text.charAt(j) == '\r')) {
                    j++;
                }

                if (matchesKeyword(text, j, lineEnd, "package")
                        || matchesKeyword(text, j, lineEnd, "import")) {
                    skipLine = true;
                }
            }

            if (!skipLine) {
                int pos = i;

                while (pos < lineEnd) {
                    char c = text.charAt(pos);

                    if (state == 3) {
                        if (c == '*' && pos + 1 < lineEnd
                                && text.charAt(pos + 1) == '/') {
                            state = 0;
                            pos += 2;
                        } else {
                            pos++;
                        }
                    } else if (state == 1 || state == 2) {
                        sb.append(c);

                        if (c == '\\' && pos + 1 < lineEnd) {
                            sb.append(text.charAt(pos + 1));
                            pos += 2;
                        } else {
                            if ((state == 1 && c == '"')
                                    || (state == 2 && c == '\'')) {
                                state = 0;
                            }

                            pos++;
                        }
                    } else if (state == 4) {
                        if (c == '"' && pos + 2 < lineEnd
                                && text.charAt(pos + 1) == '"'
                                && text.charAt(pos + 2) == '"') {
                            sb.append("\"\"\"");
                            pos += 3;
                            state = 0;
                        } else {
                            sb.append(c);
                            pos++;
                        }
                    } else {
                        if (c == '/' && pos + 1 < lineEnd) {
                            char next = text.charAt(pos + 1);

                            if (next == '/') {
                                pos = lineEnd;
                            } else if (next == '*') {
                                sb.append(' ');
                                state = 3;
                                pos += 2;
                            } else {
                                sb.append(c);
                                pos++;
                            }
                        } else if (c == '"' && pos + 2 < lineEnd
                                && text.charAt(pos + 1) == '"'
                                && text.charAt(pos + 2) == '"') {
                            sb.append("\"\"\"");
                            state = 4;
                            pos += 3;
                        } else if (c == '"') {
                            sb.append(c);
                            state = 1;
                            pos++;
                        } else if (c == '\'') {
                            sb.append(c);
                            state = 2;
                            pos++;
                        } else {
                            sb.append(c);
                            pos++;
                        }
                    }
                }
            }

            if (lineEnd < n) {
                sb.append('\n');
                i = lineEnd + 1;
            } else {
                i = n;
            }
        }

        StringBuilder collapsed = new StringBuilder(sb.length());

        boolean inSpace = false;

        for (int k = 0; k < sb.length(); k++) {
            char c = sb.charAt(k);

            if (c < 33) {
                if (!inSpace) {
                    collapsed.append(' ');
                    inSpace = true;
                }
            } else {
                collapsed.append(c);
                inSpace = false;
            }
        }

        return collapsed.toString().trim();
    }

    static boolean matchesKeyword(String text, int pos, int end, String keyword) {
        int len = keyword.length();
        boolean result = false;

        if (pos + len <= end) {
            if (text.regionMatches(pos, keyword, 0, len)) {
                int after = pos + len;

                if (after >= end) {
                    result = true;
                } else {
                    char c = text.charAt(after);

                    if (c == ' ' || c == '\t' || c == '\r') {
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    static int levenshtein(String a, String b, int limit) {
        int n = a.length();
        int m = b.length();

        int result = limit;

        if (Math.abs(n - m) < limit) {
            if (n == 0) {
                result = m;
            } else if (m == 0) {
                result = n;
            } else {
                if (n > m) {
                    String temp = a;
                    a = b;
                    b = temp;

                    int t = n;
                    n = m;
                    m = t;
                }

                final int INF = limit;

                int[] prev = new int[n + 1];
                int[] curr = new int[n + 1];

                Arrays.fill(prev, INF);

                for (int i = 0; i <= Math.min(n, limit - 1); i++) {
                    prev[i] = i;
                }

                boolean stopped = false;

                for (int j = 1; j <= m && !stopped; j++) {
                    int left = Math.max(1, j - limit + 1);
                    int right = Math.min(n, j + limit - 1);

                    curr[0] = j < limit ? j : INF;

                    if (left > 1) {
                        curr[left - 1] = INF;
                    }

                    int rowMin = INF;

                    for (int i = left; i <= right; i++) {
                        int cost = a.charAt(i - 1) == b.charAt(j - 1)
                                ? 0 : 1;

                        int value = Math.min(
                                Math.min(
                                        prev[i] + 1,
                                        curr[i - 1] + 1
                                ),
                                prev[i - 1] + cost
                        );

                        curr[i] = Math.min(value, INF);

                        if (curr[i] < rowMin) {
                            rowMin = curr[i];
                        }
                    }

                    if (right < n) {
                        curr[right + 1] = INF;
                    }

                    if (rowMin >= limit) {
                        stopped = true;
                    } else {
                        int[] temp = prev;
                        prev = curr;
                        curr = temp;
                    }
                }

                if (!stopped) {
                    result = prev[n];
                }
            }
        }

        return result;
    }
}
