package by.it.group510902.klimantovich.lesson01.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerB {
    // Структура для хранения пары "размер в байтах + путь к файлу"
    private static class FileData implements Comparable<FileData> {
        final int size;
        final String relPath;

        FileData(int size, String relPath) {
            this.size = size;
            this.relPath = relPath;
        }

        @Override
        public int compareTo(FileData o) {
            // Сортировка по возрастанию размера, при совпадении — по пути файла
            if (this.size != o.size) {
                return Integer.compare(this.size, o.size);
            }
            return this.relPath.compareTo(o.relPath);
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<FileData> results = new ArrayList<>();

        // Декодер игнорирует ошибки кодировки в чужих файлах, чтобы программа не падала
        var decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                try {
                    byte[] bytes = Files.readAllBytes(p);
                    String content = decoder.decode(ByteBuffer.wrap(bytes)).toString();

                    // Тестовые классы пропускаем
                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        return;
                    }

                    // Последовательно чищу код: убираю импорты, комментарии, края и пустые строки
                    String noImports = removePackageAndImports(content);
                    String noComments = removeComments(noImports);
                    String trimmed = trimControlChars(noComments);
                    String noEmptyLines = removeEmptyLines(trimmed);

                    int byteSize = noEmptyLines.getBytes(StandardCharsets.UTF_8).length;
                    String relPath = root.relativize(p).toString();
                    results.add(new FileData(byteSize, relPath));
                } catch (IOException ignored) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        results.sort(null);
        for (FileData data : results) {
            System.out.println(data.size + " " + data.relPath);
        }
    }

    // Линейный проход по строкам, отсеивающий package и import за O(n)
    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;
        while (i < n) {
            int lineEnd = text.indexOf('\n', i);
            if (lineEnd == -1) lineEnd = n;
            else lineEnd++;

            int j = i;
            while (j < lineEnd && text.charAt(j) <= 32) j++;

            boolean skip = false;
            if (j < lineEnd) {
                if (text.startsWith("package ", j) || text.startsWith("package\t", j) ||
                        text.startsWith("import ", j) || text.startsWith("import\t", j)) {
                    skip = true;
                }
            }
            if (!skip) sb.append(text, i, lineEnd);
            i = lineEnd;
        }
        return sb.toString();
    }

    // Удаляю // и /* */ за один проход O(n). Использую состояния, чтобы случайно не вырезать слэши внутри обычных строк "..."
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        // Состояния: 0 - обычный код, 1 - внутри двойных кавычек, 2 - внутри одинарных, 3 - строчный коммент, 4 - блочный коммент
        int state = 0;

        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            char next = (i + 1 < n) ? text.charAt(i + 1) : '\0';

            if (state == 0) {
                if (c == '/' && next == '/') {
                    state = 3;
                    i++; // Перепрыгиваю второй слэш
                } else if (c == '/' && next == '*') {
                    state = 4;
                    i++; // Перепрыгиваю звездочку
                } else {
                    if (c == '"') state = 1;
                    else if (c == '\'') state = 2;
                    sb.append(c);
                }
            } else if (state == 1) {
                sb.append(c);
                // Если встретили экранирование \", не закрываем строку раньше времени
                if (c == '\\' && next != '\0') {
                    sb.append(next);
                    i++;
                } else if (c == '"') {
                    state = 0;
                }
            } else if (state == 2) {
                sb.append(c);
                if (c == '\\' && next != '\0') {
                    sb.append(next);
                    i++;
                } else if (c == '\'') {
                    state = 0;
                }
            } else if (state == 3) {
                // Однострочный комментарий заканчивается только на переносе строки
                if (c == '\n') {
                    state = 0;
                    sb.append(c);
                }
            } else if (state == 4) {
                // Многострочный комментарий игнорируем вплоть до закрывающей пары */
                if (c == '*' && next == '/') {
                    state = 0;
                    i++;
                }
            }
        }
        return sb.toString();
    }

    // Обрезаю все управляющие символы и пробелы в самом начале и конце всего текста
    private static String trimControlChars(String text) {
        int start = 0;
        int end = text.length();
        while (start < end && text.charAt(start) < 33) start++;
        while (end > start && text.charAt(end - 1) < 33) end--;
        return text.substring(start, end);
    }

    // Оставляю только те строки, где есть хотя бы один видимый символ (код >= 33)
    private static String removeEmptyLines(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;
        while (i < n) {
            int lineEnd = text.indexOf('\n', i);
            if (lineEnd == -1) lineEnd = n;
            else lineEnd++;

            boolean hasVisibleChars = false;
            for (int j = i; j < lineEnd; j++) {
                if (text.charAt(j) >= 33) {
                    hasVisibleChars = true;
                    break;
                }
            }
            if (hasVisibleChars) {
                sb.append(text, i, lineEnd);
            }
            i = lineEnd;
        }
        return sb.toString();
    }
}
