package by.it.group551002.efimchikov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerC {
    private static final int MAX_EDITS = 9; // < 10

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<Item> items = new ArrayList<>();

        if (!Files.exists(root)) return;

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String text = readTextReplace(p);
                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            String processed = removePackageAndImports(text);
                            processed = removeComments(processed);
                            processed = collapseWhitespace(processed);

                            items.add(new Item(root.relativize(p).toString(), processed));
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        items.sort(Comparator.comparingInt(it -> it.text.length()));

        for (int i = 0; i < items.size(); i++) {
            Item a = items.get(i);
            for (int j = i + 1; j < items.size(); j++) {
                Item b = items.get(j);

                if (b.text.length() - a.text.length() >= 10) break;

                if (isCopy(a.text, b.text)) {
                    a.copies.add(b.path);
                    b.copies.add(a.path);
                }
            }
        }

        for (Item it : items) {
            Collections.sort(it.copies);
        }

        items.sort(Comparator.comparing(it -> it.path));

        for (Item it : items) {
            if (!it.copies.isEmpty()) {
                System.out.println(it.path);
                for (String copy : it.copies) {
                    System.out.println(copy);
                }
            }
        }
    }

    private static String readTextReplace(Path p) throws IOException {
        byte[] bytes = Files.readAllBytes(p);
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);
        try {
            return decoder.decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException e) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;

        while (i < n) {
            int lineStart = i;
            int lineEnd = i;
            while (lineEnd < n && text.charAt(lineEnd) != '\n' && text.charAt(lineEnd) != '\r') {
                lineEnd++;
            }

            int next = lineEnd;
            if (next < n) {
                if (text.charAt(next) == '\r') {
                    next++;
                    if (next < n && text.charAt(next) == '\n') next++;
                } else {
                    next++;
                }
            }

            String line = text.substring(lineStart, lineEnd);
            String t = line.stripLeading();

            boolean remove = startsWithKeyword(t, "package")
                    || startsWithKeyword(t, "import");

            if (!remove) sb.append(text, lineStart, next);
            i = next;
        }
        return sb.toString();
    }

    private static boolean startsWithKeyword(String s, String kw) {
        if (!s.startsWith(kw)) return false;
        if (s.length() == kw.length()) return true;
        char c = s.charAt(kw.length());
        return c == ' ' || c == '\t' || c == '\f' || c == '\r' || c == '\n' || c == ';';
    }

    private static String removeComments(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        int n = s.length();
        int i = 0;

        while (i < n) {
            char c = s.charAt(i);

            if (c == '"' || c == '\'') {
                char quote = c;
                sb.append(c);
                i++;

                while (i < n) {
                    char d = s.charAt(i);
                    sb.append(d);

                    if (d == '\\' && i + 1 < n) {
                        sb.append(s.charAt(i + 1));
                        i += 2;
                        continue;
                    }

                    i++;
                    if (d == quote) break;
                }
            } else if (c == '/' && i + 1 < n && s.charAt(i + 1) == '/') {
                i += 2;
                while (i < n && s.charAt(i) != '\n' && s.charAt(i) != '\r') i++;
            } else if (c == '/' && i + 1 < n && s.charAt(i + 1) == '*') {
                i += 2;
                while (i < n) {
                    if (s.charAt(i) == '*' && i + 1 < n && s.charAt(i + 1) == '/') {
                        i += 2;
                        break;
                    }

                    char d = s.charAt(i);
                    if (d == '\n' || d == '\r') {
                        sb.append(d);
                        if (d == '\r' && i + 1 < n && s.charAt(i + 1) == '\n') {
                            sb.append('\n');
                            i++;
                        }
                    }
                    i++;
                }
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    private static String collapseWhitespace(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        boolean inWhitespace = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 33) {
                if (!inWhitespace) {
                    sb.append(' ');
                    inWhitespace = true;
                }
            } else {
                sb.append(c);
                inWhitespace = false;
            }
        }
        return sb.toString().trim();
    }

    private static boolean isCopy(String a, String b) {
        int n = a.length();
        int m = b.length();

        if (Math.abs(n - m) > MAX_EDITS) return false;

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        Arrays.fill(prev, MAX_EDITS + 1);
        for (int j = 0; j <= Math.min(m, MAX_EDITS); j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            int from = Math.max(1, i - MAX_EDITS);
            int to = Math.min(m, i + MAX_EDITS);
            int startFill = Math.max(0, from - 1);
            int endFill = Math.min(m, to + 1) + 1;
            Arrays.fill(curr, startFill, endFill, MAX_EDITS + 1);
            curr[0] = i <= MAX_EDITS ? i : MAX_EDITS + 1;
            for (int j = from; j <= to; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;

                int v = prev[j] + 1;
                int v2 = curr[j - 1] + 1;
                if (v2 < v) v = v2;

                int v3 = prev[j - 1] + cost;
                if (v3 < v) v = v3;

                curr[j] = v;
            }

            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return prev[m] <= MAX_EDITS;
    }

    private static class Item {
        final String path;
        final String text;
        final List<String> copies = new ArrayList<>();

        Item(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }
}
