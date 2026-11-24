package by.it.group510902.klimantovich.lesson01.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerA {
    // Вспомогательный класс, чтобы удобно связать размер файла и его путь для сортировки
    private static class FileData implements Comparable<FileData> {
        final int size;
        final String relPath;

        FileData(int size, String relPath) {
            this.size = size;
            this.relPath = relPath;
        }

        @Override
        public int compareTo(FileData o) {
            // Сначала сравниваю по размеру в байтах, а если размеры совпали — сортирую по алфавиту
            if (this.size != o.size) {
                return Integer.compare(this.size, o.size);
            }
            return this.relPath.compareTo(o.relPath);
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        // Делаю список потокобезопасным для параллельной записи с разных ядер процессора
        List<FileData> results = Collections.synchronizedList(new ArrayList<>());

        // Обхожу все папки внутри src в параллельном режиме и беру только java-файлы
        try (Stream<Path> walk = Files.walk(root)) {
            walk.parallel().filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                try {
                    // Читаю байты и собираю строку через UTF-8 (битые символы заменяются безопасно без исключений)
                    byte[] bytes = Files.readAllBytes(p);
                    String content = new String(bytes, StandardCharsets.UTF_8);

                    // Файлы с тестами нам не нужны, сразу их отбрасываю
                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        return;
                    }

                    // Вырезаю package и импорты, а потом чищу края от невидимых символов
                    String cleaned = removePackageAndImports(content);
                    cleaned = trimControlChars(cleaned);

                    // Считаю итоговый вес текста в байтах и запоминаю относительный путь от папки src
                    int byteSize = cleaned.getBytes(StandardCharsets.UTF_8).length;
                    String relPath = root.relativize(p).toString();
                    results.add(new FileData(byteSize, relPath));
                } catch (IOException ignored) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортирую собранный список (сработает мой compareTo) и вывожу в консоль
        results.sort(null);
        for (FileData data : results) {
            System.out.println(data.size + " " + data.relPath);
        }
    }

    // Прохожу по тексту построчно за один проход O(n) и не копирую строки с package и import
    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;
        while (i < n) {
            int lineEnd = text.indexOf('\n', i);
            if (lineEnd == -1) lineEnd = n;
            else lineEnd++; // Захватываю сам перенос строки, чтобы не склеились соседние строчки

            // Проматываю пробелы и табуляции в начале строки, чтобы добраться до первого слова
            int j = i;
            while (j < lineEnd && text.charAt(j) <= 32) {
                j++;
            }

            // Проверяю, начинается ли строка с объявления пакета или импорта
            boolean isPkgOrImp = false;
            if (j < lineEnd) {
                if (text.startsWith("package ", j) || text.startsWith("package\t", j) ||
                        text.startsWith("import ", j) || text.startsWith("import\t", j)) {
                    isPkgOrImp = true;
                }
            }

            // Если это обычный код, оставляю строку в результате
            if (!isPkgOrImp) {
                sb.append(text, i, lineEnd);
            }
            i = lineEnd;
        }
        return sb.toString();
    }

    // Сдвигаю левую и правую границы, отрезая пробелы, переносы и спецсимволы с кодом меньше 33
    private static String trimControlChars(String text) {
        int start = 0;
        int end = text.length();
        while (start < end && text.charAt(start) < 33) start++;
        while (end > start && text.charAt(end - 1) < 33) end--;
        return text.substring(start, end);
    }
}
