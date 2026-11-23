package by.it.group551001.bondarenko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
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

    private static class FileData {
        final long size;
        final String relativePath;

        FileData(long size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Path.of(src);

        List<FileData> results = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                String content = readIgnoringErrors(p);
                if (content == null || content.contains("@Test") || content.contains("org.junit.Test")) {
                    return;
                }

                String processed = stripPackageAndImports(content);
                processed = trimControlChars(processed);

                long byteSize = processed.getBytes(StandardCharsets.UTF_8).length;
                String relPath = root.relativize(p).toString();
                results.add(new FileData(byteSize, relPath));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        results.sort(Comparator.comparingLong((FileData f) -> f.size)
                .thenComparing(f -> f.relativePath));

        for (FileData data : results) {
            System.out.println(data.size + " " + data.relativePath);
        }
    }

    private static String readIgnoringErrors(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.IGNORE)
                    .onUnmappableCharacter(CodingErrorAction.IGNORE);
            CharBuffer decoded = decoder.decode(ByteBuffer.wrap(bytes));
            return decoded.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private static String stripPackageAndImports(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        int i = 0;

        while (i < n) {
            int lineStart = i;
            while (i < n && text.charAt(i) != '\n') {
                i++;
            }
            int lineEnd = i;
            if (i < n && text.charAt(i) == '\n') {
                i++;
            }

            int j = lineStart;
            while (j < lineEnd && text.charAt(j) <= ' ') {
                j++;
            }

            if (startsWithKeyword(text, j, lineEnd, "package") ||
                    startsWithKeyword(text, j, lineEnd, "import")) {
                continue;
            }

            sb.append(text, lineStart, i);
        }
        return sb.toString();
    }

    private static boolean startsWithKeyword(String text, int start, int end, String keyword) {
        int kLen = keyword.length();
        if (end - start < kLen) {
            return false;
        }
        if (!text.regionMatches(start, keyword, 0, kLen)) {
            return false;
        }
        return end - start == kLen || !Character.isJavaIdentifierPart(text.charAt(start + kLen));
    }

    private static String trimControlChars(String text) {
        int start = 0;
        int end = text.length();
        while (start < end && text.charAt(start) < 33) {
            start++;
        }
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }
        return text.substring(start, end);
    }
}
