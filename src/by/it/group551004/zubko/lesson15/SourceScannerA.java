package by.it.group551004.zubko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerA {
    static class FileRecord {
        final int size;
        final String path;

        FileRecord(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<Path> javaFiles = new ArrayList<>();
        try (var walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(javaFiles::add);
        } catch (IOException e) {
            return;
        }

        List<FileRecord> records = javaFiles.parallelStream().map(p -> {
            try {
                byte[] raw = Files.readAllBytes(p);
                String content = new String(raw, StandardCharsets.UTF_8);
                if (content.contains("@Test") || content.contains("org.junit.Test")) {
                    return null;
                }

                // 1. Remove package and all imports in O(n)
                StringBuilder sb = new StringBuilder(content.length());
                String[] lines = content.split("\r?\n");
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                        continue;
                    }
                    sb.append(line).append("\n");
                }
                String text = sb.toString();

                // 2. Remove characters < 33 at beginning and end
                int start = 0;
                while (start < text.length() && text.charAt(start) < 33) {
                    start++;
                }
                int end = text.length();
                while (end > start && text.charAt(end - 1) < 33) {
                    end--;
                }
                text = text.substring(start, end);

                int size = text.getBytes(StandardCharsets.UTF_8).length;
                String relPath = root.relativize(p).toString();
                return new FileRecord(size, relPath);
            } catch (Exception e) {
                // Ignore MalformedInputException and other I/O errors
                return null;
            }
        }).filter(Objects::nonNull).toList();

        List<FileRecord> sorted = new ArrayList<>(records);
        sorted.sort((a, b) -> {
            if (a.size != b.size) {
                return Integer.compare(a.size, b.size);
            }
            return a.path.compareTo(b.path);
        });

        for (FileRecord rec : sorted) {
            System.out.println(rec.size + " " + rec.path);
        }
    }
}
