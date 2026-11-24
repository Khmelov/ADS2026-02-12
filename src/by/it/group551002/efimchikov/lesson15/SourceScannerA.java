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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerA {
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
                            String text = readTextIgnore(p);
                            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

                            String processed = trimLowChars(removePackageAndImports(text));
                            int size = processed.getBytes(StandardCharsets.UTF_8).length;
                            items.add(new Item(size, root.relativize(p).toString()));
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        items.sort(Comparator.comparingLong((Item it) -> it.size)
                .thenComparing(it -> it.path));

        for (Item it : items) {
            System.out.println(it.size + " " + it.path);
        }
    }

    private static String readTextIgnore(Path p) throws IOException {
        byte[] bytes = Files.readAllBytes(p);
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);
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

    private static String trimLowChars(String text) {
        int start = 0;
        int end = text.length();
        while (start < end && text.charAt(start) < 33) start++;
        while (end > start && text.charAt(end - 1) < 33) end--;
        return text.substring(start, end);
    }

    private static class Item {
        final int size;
        final String path;

        Item(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }
}
