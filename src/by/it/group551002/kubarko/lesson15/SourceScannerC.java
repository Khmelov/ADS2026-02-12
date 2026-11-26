package by.it.group551002.kubarko.lesson15;

import java.io.File;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Objects;

public class SourceScannerC {
    private record Item(String path, String fileName, String content) {}

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<Item> items;
        try (var walk = Files.walk(root)) {
            items = walk.filter(p -> p.toString().endsWith(".java"))
                    .parallel()
                    .map(file -> {
                        try {
                            String text = readUtf8(file);
                            if (text.contains("@Test") || text.contains("org.junit.Test")) {
                                return null;
                            }
                            text = removePackageAndImports(text);
                            text = removeComments(text);
                            text = normalize(text);
                            if (text.isEmpty()) {
                                return null;
                            }
                            return new Item(root.relativize(file).toString(),
                                    file.getFileName().toString(), text);
                        } catch (java.io.IOException ignored) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
        } catch (java.io.IOException ignored) {
            return;
        }

        Map<String, List<Item>> byName = new HashMap<>();
        for (Item item : items) {
            byName.computeIfAbsent(item.fileName(), k -> new ArrayList<>()).add(item);
        }

        Map<String, Set<String>> copies = new HashMap<>();
        byName.values().parallelStream().forEach(group -> {
            group.sort(Comparator.comparingInt(a -> a.content().length()));
            for (int i = 0; i < group.size(); i++) {
                Item first = group.get(i);
                for (int j = i + 1; j < group.size(); j++) {
                    Item second = group.get(j);
                    if (second.content().length() - first.content().length() >= 10) {
                        break;
                    }
                    if (isCopy(first.content(), second.content(), 9)) {
                        synchronized (copies) {
                            copies.computeIfAbsent(first.path(), k -> new TreeSet<>()).add(second.path());
                            copies.computeIfAbsent(second.path(), k -> new TreeSet<>()).add(first.path());
                        }
                    }
                }
            }
        });

        List<String> files = new ArrayList<>(copies.keySet());
        Collections.sort(files);
        for (String file : files) {
            System.out.println(file);
            for (String copy : copies.get(file)) {
                System.out.println(copy);
            }
        }
    }

    private static String readUtf8(Path file) throws java.io.IOException, CharacterCodingException {
        byte[] bytes = Files.readAllBytes(file);
        java.nio.charset.CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(java.nio.charset.CodingErrorAction.REPORT)
                .onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPORT);
        return decoder.decode(java.nio.ByteBuffer.wrap(bytes)).toString();
    }

    private static String removePackageAndImports(String text) {
        String[] lines = text.split("\\R");
        StringBuilder result = new StringBuilder(text.length());
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                result.append(line).append('\n');
            }
        }
        return result.toString();
    }

    private static String removeComments(String text) {
        StringBuilder result = new StringBuilder(text.length());
        boolean block = false;
        boolean line = false;
        boolean string = false;
        boolean character = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            char next = i + 1 < text.length() ? text.charAt(i + 1) : 0;

            if (line) {
                if (c == '\n' || c == '\r') {
                    line = false;
                    result.append(c);
                }
            } else if (block) {
                if (c == '*' && next == '/') {
                    block = false;
                    i++;
                }
            } else if (string) {
                result.append(c);
                if (c == '\\' && next != 0) {
                    result.append(next);
                    i++;
                } else if (c == '"') {
                    string = false;
                }
            } else if (character) {
                result.append(c);
                if (c == '\\' && next != 0) {
                    result.append(next);
                    i++;
                } else if (c == '\'') {
                    character = false;
                }
            } else if (c == '/' && next == '/') {
                line = true;
                i++;
            } else if (c == '/' && next == '*') {
                block = true;
                i++;
            } else {
                result.append(c);
                if (c == '"') string = true;
                else if (c == '\'') character = true;
            }
        }
        return result.toString();
    }

    private static String normalize(String text) {
        StringBuilder result = new StringBuilder(text.length());
        boolean previousSpace = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!previousSpace) {
                    result.append(' ');
                    previousSpace = true;
                }
            } else {
                result.append(c);
                previousSpace = false;
            }
        }
        return result.toString().trim();
    }

    private static boolean isCopy(String first, String second, int limit) {
        int n = first.length();
        int m = second.length();
        if (Math.abs(n - m) > limit) {
            return false;
        }
        if (n > m) {
            String swap = first;
            first = second;
            second = swap;
            int length = n;
            n = m;
            m = length;
        }

        int width = 2 * limit + 1;
        int[] previous = new int[width];
        int[] current = new int[width];
        for (int k = -limit; k <= limit; k++) {
            previous[k + limit] = k < 0 ? limit + 1 : k;
        }

        for (int i = 1; i <= n; i++) {
            char c1 = first.charAt(i - 1);
            boolean hasPossibleMatch = false;
            for (int k = -limit; k <= limit; k++) {
                int j = i + k;
                if (j < 0 || j > m) {
                    current[k + limit] = limit + 1;
                    continue;
                }

                int value = limit + 1;
                if (j > 0) {
                    int cost = c1 == second.charAt(j - 1) ? 0 : 1;
                    value = Math.min(value, previous[k + limit] + cost);
                }
                if (k + 1 <= limit) {
                    value = Math.min(value, previous[k + 1 + limit] + 1);
                }
                if (k - 1 >= -limit) {
                    value = Math.min(value, current[k - 1 + limit] + 1);
                }
                current[k + limit] = value;
                if (value <= limit) {
                    hasPossibleMatch = true;
                }
            }
            if (!hasPossibleMatch) {
                return false;
            }
            int[] temp = previous;
            previous = current;
            current = temp;
        }

        int difference = m - n;
        int distance = difference >= -limit && difference <= limit
                ? previous[difference + limit] : limit + 1;
        return distance < 10;
    }
}
