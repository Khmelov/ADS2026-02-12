package by.it.group551004.sheev.lesson15;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerB {

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
        boolean inBlockComment = false;

        while (i < n) {
            int lineStart = i;
            int lineEnd = text.indexOf('\n', i);
            if (lineEnd < 0) lineEnd = n;
            int nextLine = lineEnd < n ? lineEnd + 1 : n;

            boolean skipLine = false;
            if (!inBlockComment) {
                int j = lineStart;
                while (j < lineEnd && (text.charAt(j) == ' ' || text.charAt(j) == '\t')) j++;
                if (j < lineEnd) {
                    if (matchesKeyword(text, j, lineEnd, "package")
                            || matchesKeyword(text, j, lineEnd, "import")) skipLine = true;
                }
            }

            if (!skipLine) {
                int pos = lineStart;
                boolean lineDone = false;
                while (pos < lineEnd && !lineDone) {
                    char c = text.charAt(pos);

                    if (inBlockComment) {
                        if (c == '*' && pos + 1 < lineEnd && text.charAt(pos + 1) == '/') {
                            inBlockComment = false;
                            pos += 2;
                        } else {
                            pos++;
                        }
                    } else {
                        if (c == '/' && pos + 1 < lineEnd) {
                            char c2 = text.charAt(pos + 1);
                            if (c2 == '/') {
                                lineDone = true;
                            } else if (c2 == '*') {
                                inBlockComment = true;
                                pos += 2;
                            } else {
                                sb.append(c);
                                pos++;
                            }
                        } else {
                            sb.append(c);
                            pos++;
                        }
                    }
                }
                sb.append('\n');
            }
            i = nextLine;
        }

        int len = sb.length();
        int start = 0;
        while (start < len && sb.charAt(start) < 33) start++;
        int end = len;
        while (end > start && sb.charAt(end - 1) < 33) end--;

        StringBuilder result = new StringBuilder(end - start);
        int p = start;
        while (p < end) {
            int lineStart = p;
            int lineEnd = sb.indexOf("\n", p);
            if (lineEnd < 0 || lineEnd > end) lineEnd = end;
            int nextLine = lineEnd < end ? lineEnd + 1 : end;

            boolean empty = true;
            int k = lineStart;
            while (k < lineEnd && empty) {
                if (sb.charAt(k) >= 33) empty = false;
                k++;
            }
            if (!empty) {
                result.append(sb, lineStart, lineEnd);
                result.append('\n');
            }
            p = nextLine;
        }

        int rlen = result.length();
        if (rlen > 0 && result.charAt(rlen - 1) == '\n') rlen--;
        return result.substring(0, rlen);
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