package by.it.group551001.artobolevskiy.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerB {

    static class FileData {
        int size;
        String relativePath;

        FileData(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath)) {
            return;
        }

        List<FileData> results = new ArrayList<>();

        // Декодер для корректной обработки MalformedInputException
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);

        try (Stream<Path> walk = Files.walk(srcPath)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            byte[] rawBytes = Files.readAllBytes(p);
                            String content = decoder.reset().decode(ByteBuffer.wrap(rawBytes)).toString();

                            // Пропускаем тестовые файлы
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // 1. Удаляем package и import за O(n)
                            String noImports = removePackageAndImports(content);

                            // 2. Удаляем все комментарии за O(n)
                            String noComments = removeComments(noImports);

                            // 3. Удаляем все символы с кодом < 33 в начале и конце текста
                            String trimmed = trimControlChars(noComments);

                            // 4. Удаляем пустые строки
                            String cleaned = removeEmptyLines(trimmed);

                            // Рассчитываем размер в байтах
                            int byteSize = cleaned.getBytes(StandardCharsets.UTF_8).length;

                            // Относительный от src путь
                            String relPath = srcPath.relativize(p).toString();

                            results.add(new FileData(byteSize, relPath));
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортируем по размеру, затем лексикографически по пути
        results.sort(Comparator.comparingInt((FileData f) -> f.size)
                .thenComparing(f -> f.relativePath));

        for (FileData fd : results) {
            System.out.println(fd.size + " " + fd.relativePath);
        }
    }

    // 1. Удаление package и import за O(n)
    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        while (i < len) {
            int lineStart = i;
            int lineEnd = text.indexOf('\n', i);
            int nextStart = (lineEnd == -1) ? len : lineEnd + 1;
            if (lineEnd == -1) {
                lineEnd = len;
            }

            int pos = lineStart;
            while (pos < lineEnd && text.charAt(pos) <= ' ') {
                pos++;
            }

            if (!startsWithWord(text, pos, lineEnd, "package") &&
                    !startsWithWord(text, pos, lineEnd, "import")) {
                sb.append(text, lineStart, nextStart);
            }

            i = nextStart;
        }
        return sb.toString();
    }

    private static boolean startsWithWord(String text, int pos, int lineEnd, String word) {
        int wordLen = word.length();
        if (lineEnd - pos < wordLen) {
            return false;
        }
        for (int j = 0; j < wordLen; j++) {
            if (text.charAt(pos + j) != word.charAt(j)) {
                return false;
            }
        }
        if (pos + wordLen < lineEnd) {
            char nextChar = text.charAt(pos + wordLen);
            return nextChar <= ' ' || nextChar == ';';
        }
        return true;
    }

    // 2. Удаление однострочных и многострочных комментариев за O(n) с учетом строк и символов
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        boolean inString = false;
        boolean inChar = false;
        boolean inLineComment = false;
        boolean inBlockComment = false;

        while (i < len) {
            char c = text.charAt(i);
            char next = (i + 1 < len) ? text.charAt(i + 1) : '\0';

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    sb.append(c);
                }
                i++;
            } else if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i += 2;
                } else {
                    i++;
                }
            } else if (inString) {
                sb.append(c);
                if (c == '\\' && i + 1 < len) {
                    sb.append(next);
                    i += 2;
                } else {
                    if (c == '"') {
                        inString = false;
                    }
                    i++;
                }
            } else if (inChar) {
                sb.append(c);
                if (c == '\\' && i + 1 < len) {
                    sb.append(next);
                    i += 2;
                } else {
                    if (c == '\'') {
                        inChar = false;
                    }
                    i++;
                }
            } else {
                if (c == '/' && next == '/') {
                    inLineComment = true;
                    i += 2;
                } else if (c == '/' && next == '*') {
                    inBlockComment = true;
                    i += 2;
                } else {
                    if (c == '"') {
                        inString = true;
                    } else if (c == '\'') {
                        inChar = true;
                    }
                    sb.append(c);
                    i++;
                }
            }
        }
        return sb.toString();
    }

    // 3. Удаление символов с кодом < 33 в начале и конце текста
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

    // 4. Удаление пустых строк за O(n)
    private static String removeEmptyLines(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        while (i < len) {
            int lineStart = i;
            int lineEnd = text.indexOf('\n', i);
            int nextStart = (lineEnd == -1) ? len : lineEnd + 1;
            if (lineEnd == -1) {
                lineEnd = len;
            }

            // Проверяем, содержит ли строка хотя бы один видимый символ (код >= 33)
            boolean isEmpty = true;
            for (int pos = lineStart; pos < lineEnd; pos++) {
                if (text.charAt(pos) >= 33) {
                    isEmpty = false;
                    break;
                }
            }

            if (!isEmpty) {
                sb.append(text, lineStart, nextStart);
            }

            i = nextStart;
        }
        return sb.toString();
    }
}
