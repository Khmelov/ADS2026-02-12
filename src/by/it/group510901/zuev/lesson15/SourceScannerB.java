package by.it.group510901.zuev.lesson15;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerB {

    public static void main(String[] args) throws IOException {
        String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcDir);
        List<FileInfo> filesInfo = new ArrayList<>();

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

                // 1. Удалить package и import за O(n)
                StringBuilder sb = new StringBuilder();
                for (String line : content.split("\\R", -1)) {
                    String trimmed = line.stripLeading();
                    if (trimmed.startsWith("package ") && trimmed.stripTrailing().endsWith(";")) continue;
                    if (trimmed.startsWith("import ")  && trimmed.stripTrailing().endsWith(";")) continue;
                    sb.append(line).append('\n');
                }

                // 2. Удалить все комментарии за O(n)
                String noImports = sb.toString();
                String noComments = removeComments(noImports);

                // 3. Удалить символы < 33 в начале и конце
                int start = 0, end = noComments.length();
                while (start < end && noComments.charAt(start) < 33) start++;
                while (end > start && noComments.charAt(end - 1) < 33) end--;
                String cleaned = noComments.substring(start, end);

                // 4. Удалить пустые строки
                StringBuilder result = new StringBuilder();
                for (String line : cleaned.split("\\R", -1)) {
                    if (!line.trim().isEmpty()) {
                        result.append(line).append('\n');
                    }
                }
                String finalText = result.toString();
                // Ещё раз обрезать хвостовой \n
                start = 0;
                end = finalText.length();
                while (start < end && finalText.charAt(start) < 33) start++;
                while (end > start && finalText.charAt(end - 1) < 33) end--;
                finalText = finalText.substring(start, end);

                int sizeBytes = finalText.getBytes(Charset.forName("UTF-8")).length;
                String relative = srcPath.relativize(file).toString();
                filesInfo.add(new FileInfo(sizeBytes, relative));
            }
        }

        filesInfo.sort(Comparator.comparingInt((FileInfo f) -> f.size).thenComparing(f -> f.path));

        for (FileInfo fi : filesInfo) {
            System.out.println(fi.size + " " + fi.path);
        }
    }

    static String removeComments(String text) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int n = text.length();
        boolean inString = false;
        boolean inChar = false;

        while (i < n) {
            if (!inString && !inChar && i + 1 < n && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
                // Однострочный комментарий — пропускаем до конца строки
                while (i < n && text.charAt(i) != '\n') i++;
            } else if (!inString && !inChar && i + 1 < n && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
                // Многострочный комментарий — пропускаем до */
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

    private static class FileInfo {
        final int size;
        final String path;
        FileInfo(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }
}