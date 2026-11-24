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
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerB {

    private static final class Item {
        final String path;
        final long size;

        Item(String path, long size) {
            this.path = path;
            this.size = size;
        }
    }

    private static final Charset[] CHARSETS = {
            StandardCharsets.UTF_8,
            Charset.forName("windows-1251"),
            StandardCharsets.ISO_8859_1
    };

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

        List<Item> items = new ArrayList<>();
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
            text = stripHeaderAndBlankLines(text);
            text = trimControl(text);

            long size = text.getBytes(StandardCharsets.UTF_8).length;
            items.add(new Item(root.relativize(p).toString(), size));
        }

        items.sort((a, b) -> {
            int c = Long.compare(a.size, b.size);
            return c != 0 ? c : a.path.compareTo(b.path);
        });

        for (Item it : items) {
            System.out.println(it.size + " " + it.path);
        }
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

    private static String stripHeaderAndBlankLines(String text) {
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
            boolean blank = (i == next);
            if (!blank
                    && !startsWithWord(text, i, next, "package")
                    && !startsWithWord(text, i, next, "import")) {
                sb.append(text, pos, next);
            }
            pos = next;
        }
        return sb.toString();
    }

    private static String trimControl(String s) {
        int start = 0;
        int stop = s.length();
        while (start < stop && s.charAt(start) < 33) {
            start++;
        }
        while (stop > start && s.charAt(stop - 1) < 33) {
            stop--;
        }
        return s.substring(start, stop);
    }

    private static boolean startsWithWord(String s, int from, int limit, String word) {
        int len = word.length();
        return from + len < limit
                && s.startsWith(word, from)
                && s.charAt(from + len) <= ' ';
    }
}
