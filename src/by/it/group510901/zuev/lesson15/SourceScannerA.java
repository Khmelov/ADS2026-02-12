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

public class SourceScannerA {

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

                StringBuilder sb = new StringBuilder();
                for (String line : content.split("\\R", -1)) {
                    String trimmed = line.stripLeading();
                    if (trimmed.startsWith("package ") && trimmed.stripTrailing().endsWith(";")) {
                        continue;
                    }
                    if (trimmed.startsWith("import ") && trimmed.stripTrailing().endsWith(";")) {
                        continue;
                    }
                    sb.append(line).append('\n');
                }

                String cleaned = sb.toString();

                int start = 0;
                int end = cleaned.length();
                while (start < end && cleaned.charAt(start) < 33) start++;
                while (end > start && cleaned.charAt(end - 1) < 33) end--;
                cleaned = cleaned.substring(start, end);

                int sizeBytes = cleaned.getBytes(Charset.forName("UTF-8")).length;
                String relative = srcPath.relativize(file).toString();
                filesInfo.add(new FileInfo(sizeBytes, relative));
            }
        }

        filesInfo.sort(Comparator.comparingInt((FileInfo f) -> f.size).thenComparing(f -> f.path));

        for (FileInfo fi : filesInfo) {
            System.out.println(fi.size + " " + fi.path);
        }
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
