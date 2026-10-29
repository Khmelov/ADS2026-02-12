package by.it.group551004.zubko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerB {
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
                String[] rawLines = content.split("\r?\n");
                for (String line : rawLines) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                        continue;
                    }
                    sb.append(line).append("\n");
                }

                // 2. Remove all comments in O(n)
                String withoutComments = removeComments(sb.toString());

                // 4. Remove empty lines
                StringBuilder nonBlank = new StringBuilder(withoutComments.length());
                String[] lines = withoutComments.split("\r?\n");
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        nonBlank.append(line).append("\n");
                    }
                }
                String text = nonBlank.toString();

                // 3. Remove characters < 33 at beginning and end
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
                // Correctly handle MalformedInputException and I/O errors
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

    private static String removeComments(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        int n = s.length();
        int i = 0;
        boolean inBlock = false;
        boolean inLine = false;
        boolean inStr = false;
        boolean inChar = false;

        while (i < n) {
            char c = s.charAt(i);
            if (inLine) {
                if (c == '\n') {
                    inLine = false;
                    sb.append(c);
                }
            } else if (inBlock) {
                if (c == '*' && i + 1 < n && s.charAt(i + 1) == '/') {
                    inBlock = false;
                    i++;
                }
            } else if (inStr) {
                sb.append(c);
                if (c == '\\' && i + 1 < n) {
                    sb.append(s.charAt(i + 1));
                    i++;
                } else if (c == '"') {
                    inStr = false;
                }
            } else if (inChar) {
                sb.append(c);
                if (c == '\\' && i + 1 < n) {
                    sb.append(s.charAt(i + 1));
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
            } else {
                if (c == '/' && i + 1 < n && s.charAt(i + 1) == '/') {
                    inLine = true;
                    i++;
                } else if (c == '/' && i + 1 < n && s.charAt(i + 1) == '*') {
                    inBlock = true;
                    i++;
                } else {
                    sb.append(c);
                    if (c == '"') inStr = true;
                    else if (c == '\'') inChar = true;
                }
            }
            i++;
        }
        return sb.toString();
    }
}
