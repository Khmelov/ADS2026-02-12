package by.it.group551002.litovchenko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerC {

    public static void main(String[] args) throws IOException {
        String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcDir);
        List<FileEntry> entries = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(srcPath)) {
            List<Path> javaFiles = walk
                    .filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".java"))
                    .collect(Collectors.toList());

            for (Path file : javaFiles) {
                String content;
                try {
                    content = Files.readString(file, Charset.forName("UTF-8"));
                } catch (MalformedInputException e) {
                    continue;
                }

                if (content.contains("@Test") || content.contains("org.junit.Test")) {
                    continue;
                }

                // 1. Удалить package и import
                StringBuilder sb = new StringBuilder();
                for (String line : content.split("\\R", -1)) {
                    String trimmed = line.stripLeading();
                    if (trimmed.startsWith("package ") && trimmed.stripTrailing().endsWith(";")) continue;
                    if (trimmed.startsWith("import ")  && trimmed.stripTrailing().endsWith(";")) continue;
                    sb.append(line).append('\n');
                }

                // 2. Удалить все комментарии за O(n)
                String noComments = removeComments(sb.toString());

                // 3. Заменить все последовательности символов с кодом <33 на один пробел
                StringBuilder collapsed = new StringBuilder();
                boolean wasControl = false;
                for (int i = 0; i < noComments.length(); i++) {
                    char c = noComments.charAt(i);
                    if (c < 33) {
                        if (!wasControl) {
                            collapsed.append(' ');
                            wasControl = true;
                        }
                    } else {
                        collapsed.append(c);
                        wasControl = false;
                    }
                }

                // 4. trim()
                String cleaned = collapsed.toString().trim();

                String relative = srcPath.relativize(file).toString();
                entries.add(new FileEntry(relative, cleaned));
            }
        }

        // Сортируем пути для детерминированного вывода
        entries.sort(Comparator.comparing(e -> e.path));

        // Находим пары с расстоянием Левенштейна < 10
        // TreeMap для упорядоченного вывода: ключ = путь, значение = список копий
        TreeMap<String, List<String>> copies = new TreeMap<>();

        for (int i = 0; i < entries.size(); i++) {
            for (int j = i + 1; j < entries.size(); j++) {
                int dist = levenshteinBounded(entries.get(i).text, entries.get(j).text, 10);
                if (dist < 10) {
                    copies.computeIfAbsent(entries.get(i).path, k -> new ArrayList<>()).add(entries.get(j).path);
                    copies.computeIfAbsent(entries.get(j).path, k -> new ArrayList<>()).add(entries.get(i).path);
                }
            }
        }

        // Вывод: файл, затем его копии (тоже отсортированы)
        for (var entry : copies.entrySet()) {
            System.out.println(entry.getKey());
            List<String> sorted = entry.getValue();
            sorted.sort(String::compareTo);
            for (String copy : sorted) {
                System.out.println(copy);
            }
        }
    }

    // Расстояние Левенштейна с ранним выходом при превышении порога
    static int levenshteinBounded(String a, String b, int bound) {
        int la = a.length(), lb = b.length();
        if (Math.abs(la - lb) >= bound) return bound;

        int[] prev = new int[lb + 1];
        int[] curr = new int[lb + 1];

        for (int j = 0; j <= lb; j++) prev[j] = j;

        for (int i = 1; i <= la; i++) {
            curr[0] = i;
            int minInRow = curr[0];
            for (int j = 1; j <= lb; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                if (curr[j] < minInRow) minInRow = curr[j];
            }
            if (minInRow >= bound) return bound;
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return prev[lb];
    }

    static String removeComments(String text) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int n = text.length();
        boolean inString = false;
        boolean inChar = false;

        while (i < n) {
            if (!inString && !inChar && i + 1 < n && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
                while (i < n && text.charAt(i) != '\n') i++;
            } else if (!inString && !inChar && i + 1 < n && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < n && !(text.charAt(i) == '*' && text.charAt(i + 1) == '/')) i++;
                if (i + 1 < n) i += 2;
            } else {
                char c = text.charAt(i);
                if (c == '"' && !inChar) {
                    inString = !inString;
                } else if (c == '\'' && !inString) {
                    inChar = !inChar;
                } else if (c == '\\' && (inString || inChar) && i + 1 < n) {
                    sb.append(c);
                    i++;
                    sb.append(text.charAt(i));
                    i++;
                    continue;
                }
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    private static class FileEntry {
        final String path;
        final String text;
        FileEntry(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }
}
