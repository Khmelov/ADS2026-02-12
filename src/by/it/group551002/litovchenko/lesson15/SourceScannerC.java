package by.it.group551002.litovchenko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerC {

    private static final int BOUND = 10;
    private static final Pattern LINE_SPLIT = Pattern.compile("\\R");

    public static void main(String[] args) throws IOException {
        String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(srcDir);

        List<FileEntry> entries;
        try (Stream<Path> walk = Files.walk(srcPath)) {
            entries = walk
                    .filter(p -> p.toString().endsWith(".java"))
                    .filter(Files::isRegularFile)
                    .parallel()
                    .map(p -> processFile(srcPath, p))
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
        }

        // 1. Сортировка по длине — позволяет отсечь пары по длине двумя указателями.
        entries.sort(Comparator.comparingInt(e -> e.text.length()));

        TreeMap<String, List<String>> copies = new TreeMap<>();

        // 2. Двухуровневый проход:
        //    - внешний по i, внутренний по j (j > i), пока разница длин < BOUND
        //    - дешёвый гистограммный предфильтр до Левенштейна
        int n = entries.size();
        for (int i = 0; i < n; i++) {
            FileEntry a = entries.get(i);
            int la = a.text.length();

            for (int j = i + 1; j < n; j++) {
                FileEntry b = entries.get(j);
                int lb = b.text.length();

                // Поскольку отсортировано по длине, дальнейшие j только длиннее.
                if (lb - la >= BOUND) break;

                // Гистограммный предфильтр (нижняя граница Левенштейна).
                if (!mayBeClose(a.hist, b.hist, BOUND)) continue;

                int dist = levenshteinBounded(a.text, b.text, BOUND);
                if (dist < BOUND) {
                    copies.computeIfAbsent(a.path, k -> new ArrayList<>()).add(b.path);
                    copies.computeIfAbsent(b.path, k -> new ArrayList<>()).add(a.path);
                }
            }
        }

        for (var entry : copies.entrySet()) {
            System.out.println(entry.getKey());
            List<String> sorted = entry.getValue();
            sorted.sort(String::compareTo);
            for (String copy : sorted) {
                System.out.println(copy);
            }
        }
    }

    // ---------- Обработка одного файла ----------

    private static FileEntry processFile(Path srcPath, Path file) {
        String content;
        try {
            content = Files.readString(file, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        // Быстрый отсев тестов до тяжёлой обработки.
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return null;
        }

        // 1. Удаление package/import.
        String withoutHeader = removePackageAndImports(content);

        // 2. Удаление комментариев (одним проходом, копируя срезами).
        String noComments = removeComments(withoutHeader);

        // 3. Схлопывание control-символов в один пробел + trim, с нормализацией EOL.
        String cleaned = collapseControlChars(noComments);

        String relative = srcPath.relativize(file).toString();
        return new FileEntry(relative, cleaned);
    }

    // ---------- Удаление package / import ----------

    private static String removePackageAndImports(String content) {
        StringBuilder sb = new StringBuilder(content.length());
        int start = 0;
        int n = content.length();
        for (int i = 0; i <= n; i++) {
            if (i == n || content.charAt(i) == '\n') {
                int lineStart = start;
                int lineEnd = i; // не включая '\n'
                // Срезаем trailing '\r' от CRLF, но оставим '\n' в выводе.
                int trimmedEnd = lineEnd;
                if (trimmedEnd > lineStart && content.charAt(trimmedEnd - 1) == '\r') {
                    trimmedEnd--;
                }
                // Пропускаем ведущие пробелы/табы для проверки директивы.
                int k = lineStart;
                while (k < trimmedEnd && (content.charAt(k) == ' ' || content.charAt(k) == '\t')) k++;

                boolean drop = false;
                if (startsWith(content, k, trimmedEnd, "package ")) {
                    int last = trimmedEnd - 1;
                    while (last >= k && (content.charAt(last) == ' ' || content.charAt(last) == '\t')) last--;
                    if (last >= k && content.charAt(last) == ';') drop = true;
                } else if (startsWith(content, k, trimmedEnd, "import ")) {
                    int last = trimmedEnd - 1;
                    while (last >= k && (content.charAt(last) == ' ' || content.charAt(last) == '\t')) last--;
                    if (last >= k && content.charAt(last) == ';') drop = true;
                }

                if (!drop) {
                    sb.append(content, lineStart, lineEnd);
                    sb.append('\n');
                }
                start = i + 1;
            }
        }
        return sb.toString();
    }

    private static boolean startsWith(String s, int from, int to, String prefix) {
        if (to - from < prefix.length()) return false;
        for (int i = 0; i < prefix.length(); i++) {
            if (s.charAt(from + i) != prefix.charAt(i)) return false;
        }
        return true;
    }

    // ---------- Удаление комментариев ----------

    /**
     * Один проход. Копирует срезами через StringBuilder.append(seq, start, end),
     * что в разы быстрее посимвольного append.
     * Корректно обрабатывает строки, char-литералы, escape-последовательности
     * и text blocks (Java 15+).
     */
    static String removeComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;
        int lastCopied = 0;

        boolean inString = false;      // " ... "
        boolean inChar = false;        // ' ... '
        boolean inTextBlock = false;   // """ ... """

        while (i < n) {
            char c = text.charAt(i);

            if (inTextBlock) {
                // Ищем закрывающие """
                if (c == '"' && i + 2 < n
                        && text.charAt(i + 1) == '"' && text.charAt(i + 2) == '"') {
                    inTextBlock = false;
                    i += 3;
                } else if (c == '\\' && i + 1 < n) {
                    i += 2; // пропускаем escape
                } else {
                    i++;
                }
                continue;
            }

            if (inString) {
                if (c == '\\' && i + 1 < n) {
                    i += 2;
                } else if (c == '"') {
                    inString = false;
                    i++;
                } else {
                    i++;
                }
                continue;
            }

            if (inChar) {
                if (c == '\\' && i + 1 < n) {
                    i += 2;
                } else if (c == '\'') {
                    inChar = false;
                    i++;
                } else {
                    i++;
                }
                continue;
            }

            // Вне строк/символов
            if (c == '/' && i + 1 < n) {
                char c2 = text.charAt(i + 1);
                if (c2 == '/') {
                    // Копируем всё до комментария
                    sb.append(text, lastCopied, i);
                    // Пропускаем до '\n' (не включая его)
                    int j = i + 2;
                    while (j < n && text.charAt(j) != '\n') j++;
                    i = j;
                    lastCopied = i;
                    continue;
                }
                if (c2 == '*') {
                    sb.append(text, lastCopied, i);
                    int j = i + 2;
                    while (j + 1 < n && !(text.charAt(j) == '*' && text.charAt(j + 1) == '/')) j++;
                    i = (j + 1 < n) ? j + 2 : n;
                    lastCopied = i;
                    continue;
                }
            }

            if (c == '"') {
                if (i + 2 < n && text.charAt(i + 1) == '"' && text.charAt(i + 2) == '"') {
                    inTextBlock = true;
                    i += 3;
                    continue;
                }
                inString = true;
                i++;
                continue;
            }

            if (c == '\'') {
                inChar = true;
                i++;
                continue;
            }

            i++;
        }

        sb.append(text, lastCopied, n);
        return sb.toString();
    }

    // ---------- Схлопывание control-символов ----------

    private static String collapseControlChars(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int n = text.length();
        boolean wasControl = false;
        for (int i = 0; i < n; i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!wasControl) {
                    sb.append(' ');
                    wasControl = true;
                }
            } else {
                sb.append(c);
                wasControl = false;
            }
        }
        // trim() по краям
        int len = sb.length();
        int start = 0;
        int end = len;
        while (start < end && sb.charAt(start) <= ' ') start++;
        while (end > start && sb.charAt(end - 1) <= ' ') end--;
        return sb.substring(start, end);
    }

    // ---------- Гистограмма символов ----------

    /**
     * Гистограмма по первым 256 кодам. Для символов с кодом >= 256 — отдельный bucket.
     * Используется для быстрой нижней оценки Левенштейна.
     */
    private static int[] histogram(String s) {
        int[] h = new int[257];
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c < 256) h[c]++;
            else h[256]++;
        }
        return h;
    }

    /**
     * Нижняя граница Левенштейна: (1/2) * L1-расстояние гистограмм.
     * Если она >= bound — точное расстояние точно >= bound, можно не считать.
     */
    private static boolean mayBeClose(int[] ha, int[] hb, int bound) {
        int diff = 0;
        int limit = 2 * bound;
        for (int i = 0; i < ha.length; i++) {
            int d = ha[i] - hb[i];
            if (d < 0) d = -d;
            diff += d;
            if (diff >= limit) return false;
        }
        return true;
    }

    // ---------- Левенштейн с буферами и обрезкой префикса/суффикса ----------

    // Буферы переиспользуются между вызовами (однопоточный этап сравнения).
    private static int[] prevBuf = new int[0];
    private static int[] currBuf = new int[0];

    static int levenshteinBounded(String a, String b, int bound) {
        int la = a.length(), lb = b.length();
        if (Math.abs(la - lb) >= bound) return bound;

        // Обрезка общего префикса
        int p = 0;
        while (p < la && p < lb && a.charAt(p) == b.charAt(p)) p++;

        // Обрезка общего суффикса (не пересекаясь с префиксом)
        int s = 0;
        while (s < la - p && s < lb - p
                && a.charAt(la - 1 - s) == b.charAt(lb - 1 - s)) s++;

        int aStart = p, aEnd = la - s;
        int bStart = p, bEnd = lb - s;
        int aLen = aEnd - aStart;
        int bLen = bEnd - bStart;

        if (aLen == 0) return bLen;
        if (bLen == 0) return aLen;
        if (Math.abs(aLen - bLen) >= bound) return bound;

        // Обеспечиваем b как более короткую строку для меньшего буфера
        // (не обязательно, но уменьшает память при узких случаях)
        if (bLen > aLen) {
            // swap
            int t = aLen; aLen = bLen; bLen = t;
            int ta = aStart; aStart = bStart; bStart = ta;
            int tb = aEnd; aEnd = bEnd; bEnd = tb;
            String ts = a; a = b; b = ts;
        }

        int need = bLen + 1;
        if (prevBuf.length < need) {
            prevBuf = new int[need];
            currBuf = new int[need];
        }
        int[] prev = prevBuf;
        int[] curr = currBuf;

        for (int j = 0; j <= bLen; j++) prev[j] = j;

        for (int i = 1; i <= aLen; i++) {
            curr[0] = i;
            int minInRow = curr[0];
            char ca = a.charAt(aStart + i - 1);
            for (int j = 1; j <= bLen; j++) {
                int cost = (ca == b.charAt(bStart + j - 1)) ? 0 : 1;
                int v = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                curr[j] = v;
                if (v < minInRow) minInRow = v;
            }
            if (minInRow >= bound) return bound;
            int[] tmp = prev; prev = curr; curr = tmp;
            // Обновляем глобальные ссылки, чтобы не потерять буферы
            prevBuf = prev;
            currBuf = curr;
        }
        return prev[bLen];
    }

    // ---------- DTO ----------

    private static class FileEntry {
        final String path;
        final String text;
        final int[] hist;

        FileEntry(String path, String text) {
            this.path = path;
            this.text = text;
            this.hist = histogram(text);
        }
    }
}