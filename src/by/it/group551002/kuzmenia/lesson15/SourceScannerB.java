package by.it.group551002.kuzmenia.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerB {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Path.of(src);

        // Обходим каталог src и собираем обработанные java-файлы
        List<FileEntry> files = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String text = readFile(p);
                            // Пропускаем тестовые файлы
                            if (text.contains("@Test") || text.contains("org.junit.Test")) {
                                return;
                            }
                            String cleaned = removeComments(text);
                            cleaned = removePackageImportsAndEmptyLines(cleaned);
                            cleaned = trimControlChars(cleaned);
                            int byteSize = cleaned.getBytes(StandardCharsets.UTF_8).length;
                            String relativePath = root.relativize(p).toString();
                            files.add(new FileEntry(byteSize, relativePath));
                        } catch (IOException ignored) {
                            // Пропускаем файлы, которые не удалось прочитать
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Сортировка по размеру, при равенстве — лексикографически по пути
        files.sort((a, b) -> {
            int cmp = Integer.compare(a.size, b.size);
            return cmp != 0 ? cmp : a.path.compareTo(b.path);
        });

        // Постройка и вывод результата
        StringBuilder result = new StringBuilder();
        for (FileEntry entry : files) {
            result.append(entry.size).append(' ').append(entry.path).append('\n');
        }
        System.out.print(result);
    }

    // Корректное чтение файла с обработкой MalformedInputException
    private static String readFile(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);
        return decoder.decode(ByteBuffer.wrap(bytes)).toString();
    }

    // Удаление однострочных и многострочных комментариев за O(n)
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;

        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    sb.append(c);
                }
            } else if (inBlockComment) {
                if (c == '*' && i + 1 < n && text.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i++;
                }
            } else if (inString) {
                sb.append(c);
                if (c == '\\' && i + 1 < n) {
                    sb.append(text.charAt(++i));
                } else if (c == '"') {
                    inString = false;
                }
            } else if (inChar) {
                sb.append(c);
                if (c == '\\' && i + 1 < n) {
                    sb.append(text.charAt(++i));
                } else if (c == '\'') {
                    inChar = false;
                }
            } else {
                if (c == '/' && i + 1 < n && text.charAt(i + 1) == '/') {
                    inLineComment = true;
                    i++;
                } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    i++;
                } else if (c == '"') {
                    inString = true;
                    sb.append(c);
                } else if (c == '\'') {
                    inChar = true;
                    sb.append(c);
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    // Удаление package, импортов и пустых строк за O(n)
    private static String removePackageImportsAndEmptyLines(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;
        while (i < n) {
            int lineStart = i;
            while (i < n && text.charAt(i) != '\n') {
                i++;
            }
            int lineEnd = i;
            if (i < n && text.charAt(i) == '\n') {
                i++;
            }
            if (isBlankRange(text, lineStart, lineEnd)
                    || isPackageOrImport(text, lineStart, lineEnd)) {
                continue;
            }
            sb.append(text, lineStart, i);
        }
        return sb.toString();
    }

    // Проверка, состоит ли строка только из пробельных/управляющих символов
    private static boolean isBlankRange(String text, int start, int end) {
        for (int i = start; i < end; i++) {
            if (text.charAt(i) >= 33) {
                return false;
            }
        }
        return true;
    }

    // Проверка, является ли строка объявлением package или import
    private static boolean isPackageOrImport(String text, int start, int end) {
        int i = start;
        while (i < end && (text.charAt(i) == ' ' || text.charAt(i) == '\t')) {
            i++;
        }
        return startsWithKeyword(text, i, end, "package")
                || startsWithKeyword(text, i, end, "import");
    }

    // Проверка совпадения ключевого слова с разделителем после него
    private static boolean startsWithKeyword(String text, int offset, int end, String keyword) {
        int len = keyword.length();
        if (offset + len > end || !text.regionMatches(offset, keyword, 0, len)) {
            return false;
        }
        if (offset + len == end) {
            return true;
        }
        char next = text.charAt(offset + len);
        return next <= 32 || next == ';';
    }

    // Удаление символов с кодом < 33 в начале и конце текста
    private static String trimControlChars(String text) {
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

    private static class FileEntry {
        final int size;
        final String path;

        FileEntry(int size, String path) {
            this.size = size;
            this.path = path;
        }
    }
}