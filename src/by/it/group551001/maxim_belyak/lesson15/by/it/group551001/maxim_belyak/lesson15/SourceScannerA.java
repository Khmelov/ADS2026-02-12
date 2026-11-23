package by.it.group551001.maxim_belyak.lesson15;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {

    private record ScannedFile(String relativePath, int byteSize) {
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath) || !Files.isDirectory(srcPath)) {
            return;
        }

        List<Path> javaFiles = new ArrayList<>();
        try {
            Files.walkFileTree(srcPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (attrs.isRegularFile() && file.getFileName().toString().endsWith(".java")) {
                        javaFiles.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            return;
        }

        List<ScannedFile> results = new ArrayList<>();
        for (Path file : javaFiles) {
            String content = readFileIgnoringMalformedInput(file);
            if (content == null) {
                continue;
            }

            // Тесты не участвуют в обработке
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                continue;
            }

            String withoutPackageAndImports = removePackageAndImportsLinear(content);
            String trimmed = trimControlAndWhitespace(withoutPackageAndImports);

            byte[] utf8Bytes = trimmed.getBytes(StandardCharsets.UTF_8);
            String relativePath = srcPath.relativize(file).toString();
            results.add(new ScannedFile(relativePath, utf8Bytes.length));
        }

        results.sort(
                Comparator.comparingInt(ScannedFile::byteSize)
                        .thenComparing(ScannedFile::relativePath)
        );

        for (ScannedFile item : results) {
            System.out.println(item.byteSize() + " " + item.relativePath());
        }
    }

    private static String readFileIgnoringMalformedInput(Path file) {
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);

        StringBuilder sb = new StringBuilder();
        try (InputStream in = Files.newInputStream(file);
             Reader reader = new BufferedReader(new InputStreamReader(in, decoder))) {
            char[] buffer = new char[4096];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, read);
            }
            return sb.toString();
        } catch (MalformedInputException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Удаляет строки с package и import за один линейный проход O(n).
     */
    private static String removePackageAndImportsLinear(String text) {
        int n = text.length();
        StringBuilder result = new StringBuilder(n);

        int lineStart = 0;
        while (lineStart <= n) {
            int lineEnd = text.indexOf('\n', lineStart);
            if (lineEnd == -1) {
                lineEnd = n;
            }

            int contentStart = lineStart;
            while (contentStart < lineEnd && text.charAt(contentStart) < 33) {
                contentStart++;
            }

            boolean isPackage = text.startsWith("package ", contentStart);
            boolean isImport = text.startsWith("import ", contentStart);

            if (!isPackage && !isImport) {
                result.append(text, lineStart, lineEnd);
                if (lineEnd < n) {
                    result.append('\n');
                }
            }

            if (lineEnd == n) {
                break;
            }
            lineStart = lineEnd + 1;
        }

        return result.toString();
    }

    /**
     * Удаляет все символы с кодом < 33 в начале и конце строки.
     */
    private static String trimControlAndWhitespace(String text) {
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