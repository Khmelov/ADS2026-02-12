package by.it.group551004.sheev.lesson15;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerA {

    static class Entry {
        String path;
        long size;
        Entry(String path, long size) { this.path = path; this.size = size; }
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
                if (e != null) entries.add(e);
            }
        }

        entries.sort((a, b) -> {
            int cmp = Long.compare(a.size, b.size);
            return cmp != 0 ? cmp : a.path.compareTo(b.path);
        });

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out), 1 << 16);
        for (Entry e : entries) {
            bw.write(Long.toString(e.size));
            bw.write(' ');
            bw.write(e.path);
            bw.write('\n');
        }
        bw.flush();
    }

    static Entry readOne(Path p, Path srcPath) {
        Entry result = null;
        try {
            byte[] bytes = Files.readAllBytes(p);
            String text = new String(bytes, StandardCharsets.ISO_8859_1);
            boolean isTest = text.contains("@Test") || text.contains("org.junit.Test");
            if (!isTest) {
                String processed = process(text);
                result = new Entry(srcPath.relativize(p).toString(), processed.length());
            }
        } catch (IOException e) { }
        return result;
    }

    static String process(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        int i = 0;
        while (i < n) {
            int lineStart = i;
            int lineEnd = text.indexOf('\n', i);
            if (lineEnd < 0) lineEnd = n;
            int nextLine = lineEnd < n ? lineEnd + 1 : n;

            int j = lineStart;
            while (j < lineEnd && (text.charAt(j) == ' ' || text.charAt(j) == '\t')) j++;

            boolean skip = false;
            if (j < lineEnd) {
                if (matchesKeyword(text, j, lineEnd, "package")
                        || matchesKeyword(text, j, lineEnd, "import")) skip = true;
            }
            if (!skip) {
                sb.append(text, lineStart, lineEnd);
                if (lineEnd < n) sb.append('\n');
            }
            i = nextLine;
        }
        int len = sb.length();
        int start = 0;
        while (start < len && sb.charAt(start) < 33) start++;
        int end = len;
        while (end > start && sb.charAt(end - 1) < 33) end--;
        return sb.substring(start, end);
    }

    static boolean matchesKeyword(String text, int j, int lineEnd, String keyword) {
        int klen = keyword.length();
        boolean ok = false;
        if (j + klen <= lineEnd) {
            if (text.regionMatches(j, keyword, 0, klen)) {
                int after = j + klen;
                if (after >= lineEnd) {
                    ok = true;
                } else {
                    char c = text.charAt(after);
                    if (c == ' ' || c == '\t') ok = true;
                }
            }
        }
        return ok;
    }
}