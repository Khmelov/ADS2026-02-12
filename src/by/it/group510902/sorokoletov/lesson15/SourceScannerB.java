package by.it.group510902.sorokoletov.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerB {

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path srcPath = Paths.get(src);
        List<FileInfo> results = new ArrayList<>();

        List<Path> javaFiles;
        try (Stream<Path> stream = Files.walk(srcPath)) {
            javaFiles = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }

        for (Path file : javaFiles) {
            String content;
            try {
                byte[] bytes = Files.readAllBytes(file);
                content = new String(bytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                continue;
            }

            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                continue;
            }

            String processed = removePackageAndImports(content);
            processed = removeComments(processed);
            processed = stripLowChars(processed);
            processed = removeEmptyLines(processed);

            int sizeInBytes = processed.getBytes(StandardCharsets.UTF_8).length;
            String relPath = srcPath.relativize(file).toString();

            results.add(new FileInfo(relPath, sizeInBytes));
        }

        results.sort(Comparator
                .comparingInt((FileInfo f) -> f.size)
                .thenComparing(f -> f.path));

        for (FileInfo fi : results) {
            System.out.println(fi.size + " " + fi.path);
        }
    }

    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int i = 0;
        int n = text.length();

        while (i < n) {
            int lineEnd = text.indexOf('\n', i);
            if (lineEnd == -1) lineEnd = n;

            String line = text.substring(i, lineEnd);
            String trimmed = line.trim();

            boolean skip = trimmed.startsWith("package")
                    || trimmed.startsWith("import");

            if (!skip) {
                sb.append(line);
                if (lineEnd < n) sb.append('\n');
            }

            i = lineEnd + 1;
        }

        return sb.toString();
    }

    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;

        while (i < n) {
            char c = text.charAt(i);

            if (c == '/' && i + 1 < n) {
                char next = text.charAt(i + 1);

                if (next == '/') {
                    int end = text.indexOf('\n', i);
                    if (end == -1) end = n;
                    i = end;
                    continue;
                }

                if (next == '*') {
                    int end = text.indexOf("*/", i + 2);
                    if (end == -1) {
                        i = n;
                    } else {
                        i = end + 2;
                    }
                    continue;
                }
            }

            if (c == '"') {
                sb.append(c);
                i++;
                while (i < n) {
                    char sc = text.charAt(i);
                    sb.append(sc);
                    if (sc == '\\' && i + 1 < n) {
                        sb.append(text.charAt(i + 1));
                        i += 2;
                        continue;
                    }
                    i++;
                    if (sc == '"') break;
                }
                continue;
            }

            if (c == '\'') {
                sb.append(c);
                i++;
                while (i < n) {
                    char sc = text.charAt(i);
                    sb.append(sc);
                    if (sc == '\\' && i + 1 < n) {
                        sb.append(text.charAt(i + 1));
                        i += 2;
                        continue;
                    }
                    i++;
                    if (sc == '\'') break;
                }
                continue;
            }

            sb.append(c);
            i++;
        }

        return sb.toString();
    }

    private static String stripLowChars(String text) {
        int start = 0;
        int end = text.length();

        while (start < end && text.charAt(start) < 33) start++;
        while (end > start && text.charAt(end - 1) < 33) end--;

        return text.substring(start, end);
    }

    private static String removeEmptyLines(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int i = 0;
        int n = text.length();

        while (i < n) {
            int lineEnd = text.indexOf('\n', i);
            if (lineEnd == -1) lineEnd = n;

            String line = text.substring(i, lineEnd);

            if (!line.trim().isEmpty()) {
                sb.append(line);
                if (lineEnd < n) sb.append('\n');
            }

            i = lineEnd + 1;
        }

        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    private static class FileInfo {
        final String path;
        final int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }
}