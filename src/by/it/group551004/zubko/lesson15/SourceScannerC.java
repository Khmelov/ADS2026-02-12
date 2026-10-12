package by.it.group551004.zubko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {
    static class Item {
        final String path;
        final String fileName;
        final String content;

        Item(String path, String fileName, String content) {
            this.path = path;
            this.fileName = fileName;
            this.content = content;
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

        List<Item> items = javaFiles.parallelStream().map(p -> {
            try {
                byte[] raw = Files.readAllBytes(p);
                String rawStr = new String(raw, StandardCharsets.UTF_8);
                if (rawStr.contains("@Test") || rawStr.contains("org.junit.Test")) {
                    return null;
                }

                // 1. Remove package and imports
                StringBuilder sb = new StringBuilder(rawStr.length());
                String[] lines = rawStr.split("\r?\n");
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
                        continue;
                    }
                    sb.append(line).append("\n");
                }

                // 2. Remove comments
                String withoutComments = removeComments(sb.toString());

                // 3. Replace sequences of characters < 33 with a single space
                StringBuilder singleLine = new StringBuilder(withoutComments.length());
                boolean prevSpace = false;
                for (int i = 0; i < withoutComments.length(); i++) {
                    char c = withoutComments.charAt(i);
                    if (c < 33) {
                        if (!prevSpace) {
                            singleLine.append(' ');
                            prevSpace = true;
                        }
                    } else {
                        singleLine.append(c);
                        prevSpace = false;
                    }
                }

                // 4. trim()
                String text = singleLine.toString().trim();
                String relPath = root.relativize(p).toString();
                return new Item(relPath, p.getFileName().toString(), text);
            } catch (Exception e) {
                // Correctly handle MalformedInputException and I/O errors
                return null;
            }
        }).filter(Objects::nonNull).toList();

        Map<String, List<Item>> byName = new HashMap<>();
        for (Item item : items) {
            byName.computeIfAbsent(item.fileName, k -> new ArrayList<>()).add(item);
        }

        Map<String, Set<String>> copies = new HashMap<>();

        byName.values().parallelStream().forEach(group -> {
            if (group.size() < 2) return;
            List<Item> sorted = new ArrayList<>(group);
            sorted.sort(Comparator.comparingInt(a -> a.content.length()));

            for (int i = 0; i < sorted.size(); i++) {
                Item a = sorted.get(i);
                int lenA = a.content.length();
                for (int j = i + 1; j < sorted.size(); j++) {
                    Item b = sorted.get(j);
                    if (b.content.length() - lenA >= 10) break;
                    if (isCopy(a.content, b.content, 9)) {
                        synchronized (copies) {
                            copies.computeIfAbsent(a.path, k -> new TreeSet<>()).add(b.path);
                            copies.computeIfAbsent(b.path, k -> new TreeSet<>()).add(a.path);
                        }
                    }
                }
            }
        });

        List<String> sortedFiles = new ArrayList<>(copies.keySet());
        Collections.sort(sortedFiles);

        for (String file : sortedFiles) {
            System.out.println(file);
            for (String copy : copies.get(file)) {
                System.out.println(copy);
            }
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

    private static boolean isCopy(String s1, String s2, int limit) {
        int n = s1.length();
        int m = s2.length();
        if (Math.abs(n - m) > limit) return false;
        if (n > m) {
            String t = s1; s1 = s2; s2 = t;
            n = s1.length(); m = s2.length();
        }
        int[] prev = new int[2 * limit + 1];
        int[] curr = new int[2 * limit + 1];
        for (int k = -limit; k <= limit; k++) {
            prev[k + limit] = (k < 0) ? limit + 1 : k;
        }
        for (int i = 1; i <= n; i++) {
            char c1 = s1.charAt(i - 1);
            boolean anyValid = false;
            for (int k = -limit; k <= limit; k++) {
                int j = i + k;
                if (j < 0 || j > m) {
                    curr[k + limit] = limit + 1;
                    continue;
                }
                int res = limit + 1;
                if (j > 0) {
                    char c2 = s2.charAt(j - 1);
                    int cost = (c1 == c2) ? 0 : 1;
                    res = Math.min(res, prev[k + limit] + cost);
                }
                if (k + 1 <= limit) {
                    res = Math.min(res, prev[k + 1 + limit] + 1);
                }
                if (k - 1 >= -limit) {
                    res = Math.min(res, curr[k - 1 + limit] + 1);
                }
                curr[k + limit] = res;
                if (res <= limit) anyValid = true;
            }
            if (!anyValid) return false;
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        int diff = m - n;
        int d = (diff >= -limit && diff <= limit) ? prev[diff + limit] : limit + 1;
        return d <= limit;
    }
}
