package by.it.group510902.klimantovich.lesson01.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerC {
    // Храню путь файла и его очищенный, вытянутый в одну строчку текст для быстрого сравнения
    private static class FileItem {
        final String relPath;
        final String normalizedText;

        FileItem(String relPath, String normalizedText) {
            this.relPath = relPath;
            this.normalizedText = normalizedText;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<FileItem> files = new ArrayList<>();

        // Безопасный декодер для защиты от MalformedInputException
        var decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                try {
                    byte[] bytes = Files.readAllBytes(p);
                    String content = decoder.decode(ByteBuffer.wrap(bytes)).toString();

                    if (content.contains("@Test") || content.contains("org.junit.Test")) {
                        return;
                    }

                    // Убираю импорты, комментарии, а потом заменяю все пробелы и переносы на один пробел
                    String noImports = removePackageAndImports(content);
                    String noComments = removeComments(noImports);
                    String normalized = normalizeSpaces(noComments);

                    String relPath = root.relativize(p).toString();
                    files.add(new FileItem(relPath, normalized));
                } catch (IOException ignored) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортирую файлы по пути заранее, чтобы вывод сразу шел в лексикографическом порядке
        files.sort(Comparator.comparing(f -> f.relPath));

        int n = files.size();
        for (int i = 0; i < n; i++) {
            FileItem base = files.get(i);
            List<String> copies = new ArrayList<>();

            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                FileItem candidate = files.get(j);

                // Быстрая отсечка: если разница в длине строк >= 10, то меньше 10 правок там быть не может
                if (Math.abs(base.normalizedText.length() - candidate.normalizedText.length()) >= 10) {
                    continue;
                }

                // Запускаю оптимизированный подсчет расстояния Левенштейна
                if (levenshteinLessThan10(base.normalizedText, candidate.normalizedText)) {
                    copies.add(candidate.relPath);
                }
            }

            // Если нашлись похожие файлы, печатаю сначала сам файл, а ниже все его копии
            if (!copies.isEmpty()) {
                System.out.println(base.relPath);
                for (String copyPath : copies) {
                    System.out.println(copyPath);
                }
            }
        }
    }

    // Считаю расстояние Левенштейна только в узкой полосе матрицы, чтобы уложиться в лимит времени теста
    private static boolean levenshteinLessThan10(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (Math.abs(len1 - len2) >= 10) return false;
        if (len1 == 0) return len2 < 10;
        if (len2 == 0) return len1 < 10;

        // Делаю s1 более короткой строкой, чтобы выделить меньше памяти под массивы
        if (len1 > len2) {
            String tmp = s1; s1 = s2; s2 = tmp;
            int tmpL = len1; len1 = len2; len2 = tmpL;
        }

        // Использую только две строчки матрицы вместо огромного двумерного массива
        int[] prev = new int[len1 + 1];
        int[] curr = new int[len1 + 1];

        for (int i = 0; i <= len1; i++) prev[i] = i;

        for (int j = 1; j <= len2; j++) {
            curr[0] = j;
            char c2 = s2.charAt(j - 1);
            int minInRow = curr[0];

            // Считаю значения только в коридоре шириной +-10 вокруг главной диагонали
            int start = Math.max(1, j - 10);
            int end = Math.min(len1, j + 10);

            if (start > 1) curr[start - 1] = 10;

            for (int i = start; i <= end; i++) {
                int cost = (s1.charAt(i - 1) == c2) ? 0 : 1;
                int val = Math.min(Math.min(curr[i - 1] + 1, prev[i] + 1), prev[i - 1] + cost);
                curr[i] = val;
                if (val < minInRow) minInRow = val;
            }

            if (end < len1) curr[end + 1] = 10;

            // Если даже лучший результат в текущей строке достиг 10 правок, прерываю расчет досрочно
            if (minInRow >= 10) return false;

            int[] swap = prev;
            prev = curr;
            curr = swap;
        }

        return prev[len1] < 10;
    }

    // Удаляю строки с package и import за один линейный проход
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

    // Вырезаю все виды комментариев за O(n), не трогая содержимое строковых литералов
    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int state = 0;

        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            char next = (i + 1 < n) ? text.charAt(i + 1) : '\0';

            if (state == 0) {
                if (c == '/' && next == '/') { state = 3; i++; }
                else if (c == '/' && next == '*') { state = 4; i++; }
                else {
                    if (c == '"') state = 1;
                    else if (c == '\'') state = 2;
                    sb.append(c);
                }
            } else if (state == 1) {
                sb.append(c);
                if (c == '\\' && next != '\0') { sb.append(next); i++; }
                else if (c == '"') state = 0;
            } else if (state == 2) {
                sb.append(c);
                if (c == '\\' && next != '\0') { sb.append(next); i++; }
                else if (c == '\'') state = 0;
            } else if (state == 3) {
                if (c == '\n') { state = 0; sb.append(c); }
            } else if (state == 4) {
                if (c == '*' && next == '/') { state = 0; i++; }
            }
        }
        return sb.toString();
    }

    // Схлопываю любые последовательности символов с кодом < 33 в один пробел и сразу делаю аналог trim()
    private static String normalizeSpaces(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        // Ставлю true с самого старта, чтобы автоматически проигнорировать пробелы в начале текста
        boolean inSpace = true;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!inSpace) {
                    sb.append(' ');
                    inSpace = true;
                }
            } else {
                sb.append(c);
                inSpace = false;
            }
        }
        // Если на самом хвосте остался один пробел, отрезаю его
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
