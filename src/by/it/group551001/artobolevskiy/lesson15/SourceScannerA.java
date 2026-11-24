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

public class SourceScannerA {

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

        // Настраиваем декодер для игнорирования MalformedInputException
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
                            String processed = removePackageAndImports(content);

                            // 2. Удаляем все символы с кодом < 33 в начале и конце текста
                            processed = trimControlChars(processed);

                            // Рассчитываем размер в байтах
                            int byteSize = processed.getBytes(StandardCharsets.UTF_8).length;

                            // Получаем относительный путь от каталога src
                            String relPath = srcPath.relativize(p).toString();

                            results.add(new FileData(byteSize, relPath));
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортируем по размеру, а при совпадении — лексикографически по пути
        results.sort(Comparator.comparingInt((FileData f) -> f.size)
                .thenComparing(f -> f.relativePath));

        // Выводим результат
        for (FileData fd : results) {
            System.out.println(fd.size + " " + fd.relativePath);
        }
    }

    // Удаление строк package и import за линейное время O(n)
    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        while (i < len) {
            int lineStart = i;
            // Находим конец текущей строки
            int lineEnd = text.indexOf('\n', i);
            int nextStart;
            if (lineEnd == -1) {
                lineEnd = len;
                nextStart = len;
            } else {
                nextStart = lineEnd + 1;
            }

            // Пропускаем пробелы/табы в начале строки для проверки ключевых слов
            int pos = lineStart;
            while (pos < lineEnd && text.charAt(pos) <= ' ') {
                pos++;
            }

            boolean isPackage = startsWithWord(text, pos, lineEnd, "package");
            boolean isImport = startsWithWord(text, pos, lineEnd, "import");

            if (!isPackage && !isImport) {
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
        // После ключевого слова должен идти пробельный символ, конец строки или ';'
        if (pos + wordLen < lineEnd) {
            char nextChar = text.charAt(pos + wordLen);
            return nextChar <= ' ' || nextChar == ';';
        }
        return true;
    }

    // Удаление символов с кодом < 33 в начале и конце строки
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
}
