package by.it.group551004.kunikin.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerA {
    private static class FileEntry {
        final int byteSize;
        final String relativePath;

        FileEntry(int byteSize, String relativePath) {
            this.byteSize = byteSize;
            this.relativePath = relativePath;
        }
    }

    public static void main(String[] args) {
        String srcPath = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(srcPath);

        List<Path> javaFiles = new ArrayList<>();
        try (var walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(javaFiles::add);
        } catch (IOException e) {
            return;
        }

        List<FileEntry> entries = javaFiles.parallelStream().map(path -> {
            try {
                byte[] rawBytes = Files.readAllBytes(path);
                String source = new String(rawBytes, StandardCharsets.UTF_8);

                if (source.contains("@Test") || source.contains("org.junit.Test")) {
                    return null;
                }

                // 1. Remove package and imports in O(n)
                StringBuilder filtered = new StringBuilder(source.length());
                String[] lines = source.split("\r?\n");
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                        filtered.append(line).append('\n');
                    }
                }

                // 2. Remove characters < 33 at the beginning and end
                String cleaned = trimLowChars(filtered.toString());
                int byteLength = cleaned.getBytes(StandardCharsets.UTF_8).length;
                String relative = root.relativize(path).toString();

                return new FileEntry(byteLength, relative);
            } catch (Exception e) {
                // Ignore MalformedInputException and I/O errors
                return null;
            }
        }).filter(Objects::nonNull).toList();

        List<FileEntry> sortedList = new ArrayList<>(entries);
        sortedList.sort((a, b) -> {
            if (a.byteSize != b.byteSize) {
                return Integer.compare(a.byteSize, b.byteSize);
            }
            return a.relativePath.compareTo(b.relativePath);
        });

        for (FileEntry entry : sortedList) {
            System.out.println(entry.byteSize + " " + entry.relativePath);
        }
    }

    private static String trimLowChars(String text) {
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
