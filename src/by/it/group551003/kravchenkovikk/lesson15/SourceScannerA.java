package by.it.group551003.kravchenkovikk.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerA {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<String[]> result = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(root)) {
            for (Path path: paths.toList()) {
                if (!Files.isRegularFile(path) || !path.toString().endsWith(".java")) {
                    continue;
                }
                String text;
                try {
                    text = Files.readString(path);
                } catch (MalformedInputException e) {
                    continue;
                }

                if (text.contains("@Test") || text.contains("org.junit.Test")) {
                    continue;
                }

                StringBuilder str = new StringBuilder();
                for (String line: text.split("\n")) {
                    String sb = line.trim();
                    if (!sb.startsWith("package ") && !sb.startsWith("import ")) {
                        str.append(line).append("\n");
                    }
                }
                int start = 0;
                int end = str.length() - 1;
                while (start <= end && str.charAt(start) < 33) {
                    start++;
                }
                while (end >= start && str.charAt(end) < 33) {
                    end--;
                }
                String cleaned = (start > end) ? "" : str.substring(start, end + 1);
                int size = cleaned.getBytes(StandardCharsets.UTF_8).length;
                String relPath = root.relativize(path).toString();
                result.add(new String[]{String.valueOf(size), relPath});
            }
        } catch (IOException e) {
        }

        result.sort(Comparator.comparingInt((String[] a) -> Integer.parseInt(a[0])).thenComparing(a -> a[1]));
        for (String[] item : result) {
            System.out.println(item[0] + " " + item[1]);
        }
    }
}
