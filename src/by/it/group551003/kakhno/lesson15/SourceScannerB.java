package by.it.group551003.kakhno.lesson15;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        try {
            List<Path> javaFiles = Files.walk(Paths.get(src))
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());

            List<FileData> fileDataList = new ArrayList<>();

            for (Path filePath : javaFiles)
                try {
                    byte[] fileBytes = Files.readAllBytes(filePath);

                    CharsetDecoder decoder = java.nio.charset.StandardCharsets.UTF_8.newDecoder();
                    decoder.onMalformedInput(CodingErrorAction.IGNORE);
                    decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);

                    String content = decoder.decode(ByteBuffer.wrap(fileBytes)).toString();

                    if (content.contains("@Test") || content.contains("org.junit.Test"))
                        continue;

                    String processed = processContent(content);

                    byte[] processedBytes = processed.getBytes();
                    int size = processedBytes.length;

                    String relativePath = Paths.get(src).relativize(filePath).toString();

                    fileDataList.add(new FileData(size, relativePath));

                } catch (Exception e) {
                    continue;
                }

            Collections.sort(fileDataList, (a, b) -> {
                if (a.size != b.size)
                    return Integer.compare(a.size, b.size);
                return a.path.compareTo(b.path);
            });

            for (FileData fd : fileDataList)
                System.out.println(fd.size + " " + fd.path);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private static String processContent(String content) {
        StringBuilder result = new StringBuilder();

        String[] lines = content.split("\n", -1);

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.startsWith("package ") || trimmed.startsWith("import "))
                continue;

            String withoutComments = removeComments(line);

            if (!withoutComments.trim().isEmpty())
                result.append(withoutComments).append("\n");
        }

        String processed = result.toString();

        int start = 0;
        while (start < processed.length() && processed.charAt(start) < 33)
            start++;

        int end = processed.length() - 1;
        while (end >= start && processed.charAt(end) < 33)
            end--;

        if (start <= end)
            return processed.substring(start, end + 1);
        else
            return "";
    }

    private static String removeComments(String line) {
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inLineComment = false;

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inLineComment)
                break;

            if (c == '\'' && !inDoubleQuote)
                inSingleQuote = !inSingleQuote;
            else if (c == '"' && !inSingleQuote)
                inDoubleQuote = !inDoubleQuote;
            else if (!inSingleQuote && !inDoubleQuote) {
                if (c == '/' && i + 1 < line.length() && line.charAt(i + 1) == '/') {
                    inLineComment = true;
                    break;
                }
                if (c == '/' && i + 1 < line.length() && line.charAt(i + 1) == '*') {
                    int endComment = line.indexOf("*/", i + 2);
                    if (endComment != -1)
                        i = endComment + 1;
                    else
                        break;
                    continue;
                }
            }

            if (!inLineComment)
                result.append(c);
        }

        return result.toString();
    }

    private static class FileData {
        int size;
        String path;

        FileData(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }
}