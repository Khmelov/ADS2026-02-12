package by.it.group551002.voroshko.lesson15;


import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class SourceScannerC {

    private static final int MAX_DIST = 9; // число правок < 10

    private static class FileText {
        final String path;
        final String text;

        FileText(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }

    private static class TextGroup {
        final String text;
        final int len;
        final int[] hist;
        final List<String> paths = new ArrayList<>();

        TextGroup(String text) {
            this.text = text;
            this.len = text.length();
            this.hist = buildHistogram(text);
        }

        private static int[] buildHistogram(String s) {
            int[] h = new int[32];
            for (int i = 0; i < s.length(); i++) {
                h[s.charAt(i) & 31]++;
            }
            return h;
        }
    }

    private enum State {
        NORMAL,
        LINE_COMMENT,
        BLOCK_COMMENT,
        STRING,
        CHAR,
        TEXT_BLOCK
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath)) {
            return;
        }

        List<Path> javaFiles = new ArrayList<>(4096);
        try {
            Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (attrs.isRegularFile() && file.getFileName().toString().endsWith(".java")) {
                        javaFiles.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        int workers = Math.min(16, Math.max(4, Runtime.getRuntime().availableProcessors() * 2));
        ExecutorService pool = Executors.newFixedThreadPool(workers);

        try {
            List<FileText> fileTexts = readAndCleanInParallel(srcPath, javaFiles, pool, workers);

            Map<String, TextGroup> groupMap = new HashMap<>(fileTexts.size());
            for (FileText ft : fileTexts) {
                groupMap.computeIfAbsent(ft.text, TextGroup::new).paths.add(ft.path);
            }

            List<TextGroup> groups = new ArrayList<>(groupMap.values());
            groups.sort(Comparator.comparingInt(g -> g.len));

            List<int[]> similarGroupPairs = findSimilarGroupsInParallel(groups, pool, workers);

            Map<String, TreeSet<String>> copiesByPath = new TreeMap<>();

            for (TextGroup g : groups) {
                if (g.paths.size() > 1) {
                    for (String p : g.paths) {
                        TreeSet<String> copies = copiesByPath.computeIfAbsent(p, k -> new TreeSet<>());
                        for (String q : g.paths) {
                            if (!p.equals(q)) {
                                copies.add(q);
                            }
                        }
                    }
                }
            }

            for (int[] pair : similarGroupPairs) {
                TextGroup gi = groups.get(pair[0]);
                TextGroup gj = groups.get(pair[1]);
                for (String p : gi.paths) {
                    copiesByPath.computeIfAbsent(p, k -> new TreeSet<>()).addAll(gj.paths);
                }
                for (String q : gj.paths) {
                    copiesByPath.computeIfAbsent(q, k -> new TreeSet<>()).addAll(gi.paths);
                }
            }

            StringBuilder out = new StringBuilder();
            String nl = System.lineSeparator();
            for (Map.Entry<String, TreeSet<String>> entry : copiesByPath.entrySet()) {
                out.append(entry.getKey()).append(nl);
                for (String copyPath : entry.getValue()) {
                    out.append(copyPath).append(nl);
                }
            }
            System.out.print(out);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } finally {
            pool.shutdownNow();
        }
    }

    private static List<FileText> readAndCleanInParallel(
            Path srcPath, List<Path> javaFiles, ExecutorService pool, int workers)
            throws InterruptedException, ExecutionException {

        if (javaFiles.isEmpty()) {
            return new ArrayList<>();
        }

        AtomicInteger index = new AtomicInteger(0);
        List<Future<List<FileText>>> futures = new ArrayList<>(workers);

        for (int w = 0; w < workers; w++) {
            futures.add(pool.submit(() -> {
                CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                        .onMalformedInput(CodingErrorAction.IGNORE)
                        .onUnmappableCharacter(CodingErrorAction.IGNORE);
                List<FileText> local = new ArrayList<>();
                int i;
                while ((i = index.getAndIncrement()) < javaFiles.size()) {
                    FileText ft = processFile(srcPath, javaFiles.get(i), decoder);
                    if (ft != null) {
                        local.add(ft);
                    }
                }
                return local;
            }));
        }

        List<FileText> all = new ArrayList<>(javaFiles.size());
        for (Future<List<FileText>> f : futures) {
            all.addAll(f.get());
        }
        return all;
    }

    private static FileText processFile(Path srcPath, Path file, CharsetDecoder decoder) {
        String text;
        try {
            byte[] bytes = Files.readAllBytes(file);
            decoder.reset();
            text = decoder.decode(ByteBuffer.wrap(bytes)).toString();
        } catch (IOException e) {
            return null;
        }

        if (text.contains("@Test") || text.contains("org.junit.Test")) {
            return null;
        }

        String cleaned = cleanText(text);
        String relativePath = srcPath.relativize(file).toString();
        return new FileText(relativePath, cleaned);
    }

    private static List<int[]> findSimilarGroupsInParallel(
            List<TextGroup> groups, ExecutorService pool, int workers)
            throws InterruptedException, ExecutionException {

        int u = groups.size();
        if (u < 2) {
            return new ArrayList<>();
        }

        AtomicInteger index = new AtomicInteger(0);
        List<Future<List<int[]>>> futures = new ArrayList<>(workers);

        for (int w = 0; w < workers; w++) {
            futures.add(pool.submit(() -> {
                List<int[]> localPairs = new ArrayList<>();
                int[][] buffers = {new int[256], new int[256]};

                int i;
                while ((i = index.getAndIncrement()) < u) {
                    TextGroup gi = groups.get(i);
                    int lenI = gi.len;
                    int[] histI = gi.hist;

                    for (int j = i + 1; j < u; j++) {
                        TextGroup gj = groups.get(j);
                        int lenDiff = gj.len - lenI;
                        if (lenDiff > MAX_DIST) {
                            break;
                        }

                        int[] histJ = gj.hist;
                        int diffSum = 0;
                        for (int k = 0; k < 32; k++) {
                            diffSum += Math.abs(histI[k] - histJ[k]);
                        }
                        if (diffSum + lenDiff > MAX_DIST * 2) {
                            continue;
                        }

                        if (isWithinMaxDistance(gi.text, gj.text, buffers)) {
                            localPairs.add(new int[]{i, j});
                        }
                    }
                }
                return localPairs;
            }));
        }

        List<int[]> allPairs = new ArrayList<>();
        for (Future<List<int[]>> f : futures) {
            allPairs.addAll(f.get());
        }
        return allPairs;
    }

    private static boolean isWithinMaxDistance(String a, String b, int[][] buffers) {
        final int inf = MAX_DIST + 1;

        int lenA = a.length();
        int lenB = b.length();
        if (Math.abs(lenA - lenB) > MAX_DIST) {
            return false;
        }

        int start = 0;
        int minLen = Math.min(lenA, lenB);
        while (start < minLen && a.charAt(start) == b.charAt(start)) {
            start++;
        }

        int endA = lenA;
        int endB = lenB;
        while (endA > start && endB > start && a.charAt(endA - 1) == b.charAt(endB - 1)) {
            endA--;
            endB--;
        }

        int n = endA - start;
        int m = endB - start;
        if (n == 0) {
            return m <= MAX_DIST;
        }
        if (m == 0) {
            return n <= MAX_DIST;
        }
        if (Math.abs(n - m) > MAX_DIST) {
            return false;
        }

        if (buffers[0].length < m + 2) {
            int newSize = Math.max(m + 2, buffers[0].length * 2);
            buffers[0] = new int[newSize];
            buffers[1] = new int[newSize];
        }
        int[] prev = buffers[0];
        int[] curr = buffers[1];

        int initLimit = Math.min(m, MAX_DIST);
        for (int j = 0; j <= initLimit; j++) {
            prev[j] = j;
        }
        if (m > MAX_DIST) {
            prev[MAX_DIST + 1] = inf;
        }

        for (int i = 1; i <= n; i++) {
            int from = Math.max(1, i - MAX_DIST);
            int to = Math.min(m, i + MAX_DIST);

            curr[from - 1] = (i <= MAX_DIST) ? i : inf;

            char ca = a.charAt(start + i - 1);
            int rowMin = inf;

            for (int j = from; j <= to; j++) {
                int cost = (ca == b.charAt(start + j - 1)) ? 0 : 1;
                int del = prev[j] + 1;
                int ins = curr[j - 1] + 1;
                int sub = prev[j - 1] + cost;
                int val = Math.min(sub, Math.min(del, ins));
                curr[j] = val;
                if (val < rowMin) {
                    rowMin = val;
                }
            }

            if (to < m) {
                curr[to + 1] = inf;
            }

            if (rowMin > MAX_DIST) {
                return false;
            }

            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return prev[m] <= MAX_DIST;
    }

    private static String cleanText(String text) {
        StringBuilder noComments = removeComments(text);

        int n = noComments.length();
        StringBuilder result = new StringBuilder(n);
        boolean inSpace = true;
        int i = 0;

        while (i < n) {
            int lineStart = i;
            while (i < n && noComments.charAt(i) != '\n') {
                i++;
            }
            int lineEnd = i;
            if (i < n && noComments.charAt(i) == '\n') {
                i++;
            }

            int p = lineStart;
            while (p < lineEnd && noComments.charAt(p) < 33) {
                p++;
            }

            if (p < lineEnd && (startsWithWord(noComments, p, lineEnd, "package")
                    || startsWithWord(noComments, p, lineEnd, "import"))) {
                continue;
            }

            for (int k = lineStart; k < i; k++) {
                char c = noComments.charAt(k);
                if (c < 33) {
                    if (!inSpace) {
                        result.append(' ');
                        inSpace = true;
                    }
                } else {
                    result.append(c);
                    inSpace = false;
                }
            }
        }

        int len = result.length();
        if (len > 0 && result.charAt(len - 1) == ' ') {
            result.setLength(len - 1);
        }

        return result.toString();
    }

    private static StringBuilder removeComments(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        State state = State.NORMAL;
        int i = 0;

        while (i < n) {
            char c = text.charAt(i);
            switch (state) {
                case NORMAL:
                    if (c == '"') {
                        if (i + 2 < n && text.charAt(i + 1) == '"' && text.charAt(i + 2) == '"') {
                            sb.append("\"\"\"");
                            state = State.TEXT_BLOCK;
                            i += 3;
                        } else {
                            sb.append('"');
                            state = State.STRING;
                            i++;
                        }
                    } else if (c == '\'') {
                        sb.append('\'');
                        state = State.CHAR;
                        i++;
                    } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '/') {
                        state = State.LINE_COMMENT;
                        i += 2;
                    } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '*') {
                        state = State.BLOCK_COMMENT;
                        i += 2;
                    } else {
                        sb.append(c);
                        i++;
                    }
                    break;

                case LINE_COMMENT:
                    if (c == '\r' || c == '\n') {
                        sb.append(c);
                        state = State.NORMAL;
                    }
                    i++;
                    break;

                case BLOCK_COMMENT:
                    if (c == '*' && i + 1 < n && text.charAt(i + 1) == '/') {
                        state = State.NORMAL;
                        i += 2;
                    } else {
                        i++;
                    }
                    break;

                case STRING:
                    sb.append(c);
                    if (c == '\\' && i + 1 < n) {
                        sb.append(text.charAt(i + 1));
                        i += 2;
                    } else {
                        if (c == '"' || c == '\r' || c == '\n') {
                            state = State.NORMAL;
                        }
                        i++;
                    }
                    break;

                case CHAR:
                    sb.append(c);
                    if (c == '\\' && i + 1 < n) {
                        sb.append(text.charAt(i + 1));
                        i += 2;
                    } else {
                        if (c == '\'' || c == '\r' || c == '\n') {
                            state = State.NORMAL;
                        }
                        i++;
                    }
                    break;

                case TEXT_BLOCK:
                    if (c == '\\' && i + 1 < n) {
                        sb.append(c).append(text.charAt(i + 1));
                        i += 2;
                    } else if (c == '"' && i + 2 < n && text.charAt(i + 1) == '"' && text.charAt(i + 2) == '"') {
                        sb.append("\"\"\"");
                        state = State.NORMAL;
                        i += 3;
                    } else {
                        sb.append(c);
                        i++;
                    }
                    break;
            }
        }

        return sb;
    }

    private static boolean startsWithWord(CharSequence cs, int offset, int lineEnd, String word) {
        int len = word.length();
        if (offset + len >= lineEnd) {
            return false;
        }
        for (int j = 0; j < len; j++) {
            if (cs.charAt(offset + j) != word.charAt(j)) {
                return false;
            }
        }
        return cs.charAt(offset + len) < 33;
    }
}