package by.it.group551004.kunikin.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SourceScannerC {
    private static class JavaDocItem {
        final String relativePath;
        final String fileName;
        final String normalizedText;

        JavaDocItem(String relativePath, String fileName, String normalizedText) {
            this.relativePath = relativePath;
            this.fileName = fileName;
            this.normalizedText = normalizedText;
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

        List<JavaDocItem> items = javaFiles.parallelStream().map(path -> {
            try {
                byte[] rawBytes = Files.readAllBytes(path);
                String source = new String(rawBytes, StandardCharsets.UTF_8);

                if (source.contains("@Test") || source.contains("org.junit.Test")) {
                    return null;
                }

                // 1. Remove package and imports
                StringBuilder filtered = new StringBuilder(source.length());
                String[] lines = source.split("\r?\n");
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                        filtered.append(line).append('\n');
                    }
                }

                // 2. Remove all comments
                String withoutComments = stripComments(filtered.toString());

                // 3. Replace all sequences of chars < 33 with a single space ' '
                StringBuilder singleLine = new StringBuilder(withoutComments.length());
                boolean inWhitespace = false;
                for (int i = 0; i < withoutComments.length(); i++) {
                    char c = withoutComments.charAt(i);
                    if (c < 33) {
                        if (!inWhitespace) {
                            singleLine.append(' ');
                            inWhitespace = true;
                        }
                    } else {
                        singleLine.append(c);
                        inWhitespace = false;
                    }
                }

                // 4. trim()
                String normalized = singleLine.toString().trim();
                String relative = root.relativize(path).toString();
                String fileName = path.getFileName().toString();

                return new JavaDocItem(relative, fileName, normalized);
            } catch (Exception e) {
                // Ignore MalformedInputException and I/O errors
                return null;
            }
        }).filter(Objects::nonNull).toList();

        // Group by filename to optimize comparison
        Map<String, List<JavaDocItem>> groups = new HashMap<>();
        for (JavaDocItem item : items) {
            groups.computeIfAbsent(item.fileName, k -> new ArrayList<>()).add(item);
        }

        Map<String, Set<String>> copyMap = new HashMap<>();

        groups.values().parallelStream().forEach(group -> {
            if (group.size() < 2) {
                return;
            }
            List<JavaDocItem> list = new ArrayList<>(group);
            list.sort(Comparator.comparingInt(a -> a.normalizedText.length()));

            for (int i = 0; i < list.size(); i++) {
                JavaDocItem docA = list.get(i);
                int lenA = docA.normalizedText.length();

                for (int j = i + 1; j < list.size(); j++) {
                    JavaDocItem docB = list.get(j);
                    if (docB.normalizedText.length() - lenA >= 10) {
                        break;
                    }

                    if (isSimilar(docA.normalizedText, docB.normalizedText, 9)) {
                        synchronized (copyMap) {
                            copyMap.computeIfAbsent(docA.relativePath, k -> new TreeSet<>()).add(docB.relativePath);
                            copyMap.computeIfAbsent(docB.relativePath, k -> new TreeSet<>()).add(docA.relativePath);
                        }
                    }
                }
            }
        });

        List<String> sortedKeys = new ArrayList<>(copyMap.keySet());
        Collections.sort(sortedKeys);

        for (String originalPath : sortedKeys) {
            System.out.println(originalPath);
            for (String copyPath : copyMap.get(originalPath)) {
                System.out.println(copyPath);
            }
        }
    }

    private static String stripComments(String code) {
        StringBuilder sb = new StringBuilder(code.length());
        int len = code.length();
        int idx = 0;
        boolean singleLine = false;
        boolean multiLine = false;
        boolean inStr = false;
        boolean inChar = false;

        while (idx < len) {
            char ch = code.charAt(idx);

            if (singleLine) {
                if (ch == '\n') {
                    singleLine = false;
                    sb.append(ch);
                }
            } else if (multiLine) {
                if (ch == '*' && idx + 1 < len && code.charAt(idx + 1) == '/') {
                    multiLine = false;
                    idx++;
                }
            } else if (inStr) {
                sb.append(ch);
                if (ch == '\\' && idx + 1 < len) {
                    sb.append(code.charAt(idx + 1));
                    idx++;
                } else if (ch == '"') {
                    inStr = false;
                }
            } else if (inChar) {
                sb.append(ch);
                if (ch == '\\' && idx + 1 < len) {
                    sb.append(code.charAt(idx + 1));
                    idx++;
                } else if (ch == '\'') {
                    inChar = false;
                }
            } else {
                if (ch == '/' && idx + 1 < len && code.charAt(idx + 1) == '/') {
                    singleLine = true;
                    idx++;
                } else if (ch == '/' && idx + 1 < len && code.charAt(idx + 1) == '*') {
                    multiLine = true;
                    idx++;
                } else {
                    sb.append(ch);
                    if (ch == '"') {
                        inStr = true;
                    } else if (ch == '\'') {
                        inChar = true;
                    }
                }
            }
            idx++;
        }
        return sb.toString();
    }

    private static boolean isSimilar(String s1, String s2, int maxEdits) {
        int n = s1.length();
        int m = s2.length();
        if (Math.abs(n - m) > maxEdits) {
            return false;
        }

        if (n > m) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
            n = s1.length();
            m = s2.length();
        }

        int bandWidth = 2 * maxEdits + 1;
        int[] prev = new int[bandWidth];
        int[] curr = new int[bandWidth];

        for (int k = -maxEdits; k <= maxEdits; k++) {
            prev[k + maxEdits] = (k < 0) ? maxEdits + 1 : k;
        }

        for (int i = 1; i <= n; i++) {
            char c1 = s1.charAt(i - 1);
            boolean possible = false;

            for (int k = -maxEdits; k <= maxEdits; k++) {
                int j = i + k;
                if (j < 0 || j > m) {
                    curr[k + maxEdits] = maxEdits + 1;
                    continue;
                }

                int val = maxEdits + 1;
                if (j > 0) {
                    char c2 = s2.charAt(j - 1);
                    int cost = (c1 == c2) ? 0 : 1;
                    val = Math.min(val, prev[k + maxEdits] + cost);
                }
                if (k + 1 <= maxEdits) {
                    val = Math.min(val, prev[k + 1 + maxEdits] + 1);
                }
                if (k - 1 >= -maxEdits) {
                    val = Math.min(val, curr[k - 1 + maxEdits] + 1);
                }

                curr[k + maxEdits] = val;
                if (val <= maxEdits) {
                    possible = true;
                }
            }

            if (!possible) {
                return false;
            }

            int[] swap = prev;
            prev = curr;
            curr = swap;
        }

        int diff = m - n;
        int distance = (diff >= -maxEdits && diff <= maxEdits) ? prev[diff + maxEdits] : maxEdits + 1;
        return distance <= maxEdits;
    }
}
