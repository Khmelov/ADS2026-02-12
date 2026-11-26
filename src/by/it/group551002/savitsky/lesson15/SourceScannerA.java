package by.it.group551002.savitsky.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerA {

    private static class FileInfo {
        final String path;
        final int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
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
                    .filter(file -> file.toString().endsWith(".java"))
                    .toList();
        }

        List<FileInfo> result = new ArrayList<>();

        for (Path path : files) {
            String text = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

            if (text.contains("@Test") || text.contains("org.junit.Test")) {
                continue;
            }

            text = removePackageAndImports(text);
            text.trim();

            int size = text.getBytes(StandardCharsets.UTF_8).length;
            result.add(new FileInfo(root.relativize(path).toString(), size));
        }

        result.sort((a, b) -> {
            if (a.size != b.size) {
                return Integer.compare(a.size, b.size);
            }

            return a.path.compareTo(b.path);
        });

        for (FileInfo fileInfo : result) {
            System.out.println(fileInfo.size + " " + fileInfo.path);
        }
    }

    private static String removePackageAndImports(String text) {
        int n = text.length();
        StringBuilder sb = new StringBuilder(n);
        int pos = 0;

        while (pos < n) {
            int end = text.indexOf('\n', pos);
            int next = (end == -1) ? n : end + 1;

            int i = pos;

            while (i < next && text.charAt(i) == ' ') {
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
}
