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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Path.of(src);

        // Обходим каталог src и группируем полностью одинаковые тексты
        Map<String, List<String>> exactGroups = new HashMap<>();
        int maxLen = 0;
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
                            cleaned = removePackageAndImports(cleaned);
                            cleaned = normalizeAndTrim(cleaned);
                            String relativePath = root.relativize(p).toString();
                            exactGroups.computeIfAbsent(cleaned, k -> new ArrayList<>())
                                    .add(relativePath);
                        } catch (IOException ignored) {
                            // Пропускаем файлы, которые не удалось прочитать
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Переводим уникальные тексты в список и сортируем по длине
        List<TextGroup> groups = new ArrayList<>(exactGroups.size());
        for (Map.Entry<String, List<String>> entry : exactGroups.entrySet()) {
            String text = entry.getKey();
            if (text.length() > maxLen) {
                maxLen = text.length();
            }
            groups.add(new TextGroup(text, entry.getValue()));
        }
        groups.sort(Comparator.comparingInt(g -> g.text.length()));

        // Собираем копии: сначала точные совпадения (расстояние 0)
        Map<String, List<String>> copies = new TreeMap<>();
        for (TextGroup group : groups) {
            List<String> paths = group.paths;
            if (paths.size() > 1) {
                for (int i = 0; i < paths.size(); i++) {
                    String current = paths.get(i);
                    List<String> list = copies.computeIfAbsent(current, k -> new ArrayList<>());
                    for (int j = 0; j < paths.size(); j++) {
                        if (i != j) {
                            list.add(paths.get(j));
                        }
                    }
                }
            }
        }

        // Поиск похожих текстов с расстоянием Левенштейна < 10
        int[] prev = new int[maxLen + 1];
        int[] curr = new int[maxLen + 1];
        for (int i = 0; i < groups.size(); i++) {
            TextGroup first = groups.get(i);
            for (int j = i + 1; j < groups.size(); j++) {
                TextGroup second = groups.get(j);
                if (second.text.length() - first.text.length() >= 10) {
                    break;
                }
                if (isCopy(first.text, second.text, 9, prev, curr)) {
                    for (String p1 : first.paths) {
                        copies.computeIfAbsent(p1, k -> new ArrayList<>()).addAll(second.paths);
                    }
                    for (String p2 : second.paths) {
                        copies.computeIfAbsent(p2, k -> new ArrayList<>()).addAll(first.paths);
                    }
                }
            }
        }

        // Вывод файлов и их копий в лексикографическом порядке
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : copies.entrySet()) {
            result.append(entry.getKey()).append('\n');
            List<String> list = entry.getValue();
            Collections.sort(list);
            for (String copyPath : list) {
                result.append(copyPath).append('\n');
            }
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

    // Удаление строки package и всех импортов за O(n)
    private static String removePackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;
        while (i < n) {
            int lineStart = i;
            while (i < n && text.charAt(i) != '\n') {
                i++;
            }
            if (i < n && text.charAt(i) == '\n') {
                i++;
            }
            if (!isPackageOrImport(text, lineStart, i)) {
                sb.append(text, lineStart, i);
            }
        }
        return sb.toString();
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

    // Замена последовательностей символов с кодом < 33 на пробел (32) и trim()
    private static String normalizeAndTrim(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        boolean inControlSeq = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                inControlSeq = true;
            } else {
                if (inControlSeq && sb.length() > 0) {
                    sb.append(' ');
                }
                inControlSeq = false;
                sb.append(c);
            }
        }
        return sb.toString().trim();
    }

    // Полосатый алгоритм Левенштейна без лишних аллокаций с проверкой dist <= maxDist
    private static boolean isCopy(String a, String b, int maxDist, int[] prev, int[] curr) {
        if (Math.abs(a.length() - b.length()) > maxDist) {
            return false;
        }

        // Отбрасываем одинаковые префикс и суффикс
        int start = 0;
        int minLen = Math.min(a.length(), b.length());
        while (start < minLen && a.charAt(start) == b.charAt(start)) {
            start++;
        }
        int endA = a.length();
        int endB = b.length();
        while (endA > start && endB > start && a.charAt(endA - 1) == b.charAt(endB - 1)) {
            endA--;
            endB--;
        }

        int n = endA - start;
        int m = endB - start;
        if (n == 0) {
            return m <= maxDist;
        }
        if (m == 0) {
            return n <= maxDist;
        }
        if (Math.abs(n - m) > maxDist) {
            return false;
        }

        int inf = maxDist + 1;
        int initLimit = Math.min(m, maxDist);
        for (int j = 0; j <= initLimit; j++) {
            prev[j] = j;
        }
        if (initLimit < m) {
            prev[initLimit + 1] = inf;
        }

        for (int i = 1; i <= n; i++) {
            int from = Math.max(1, i - maxDist);
            int to = Math.min(m, i + maxDist);
            curr[0] = (i <= maxDist) ? i : inf;
            if (from > 1) {
                curr[from - 1] = inf;
            }
            if (to < m) {
                curr[to + 1] = inf;
            }

            int rowMin = curr[0];
            char ca = a.charAt(start + i - 1);
            for (int j = from; j <= to; j++) {
                int cost = (ca == b.charAt(start + j - 1)) ? 0 : 1;
                int del = prev[j] + 1;
                int ins = curr[j - 1] + 1;
                int sub = prev[j - 1] + cost;
                int val = Math.min(Math.min(del, ins), sub);
                curr[j] = val;
                if (val < rowMin) {
                    rowMin = val;
                }
            }
            if (rowMin > maxDist) {
                return false;
            }

            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        return prev[m] <= maxDist;
    }

    private static class TextGroup {
        final String text;
        final List<String> paths;

        TextGroup(String text, List<String> paths) {
            this.text = text;
            this.paths = paths;
        }
    }
}