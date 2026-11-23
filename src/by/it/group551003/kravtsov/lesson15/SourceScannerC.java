package by.it.group551003.kravtsov.lesson15;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerC {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);
        List<ProcessedFile> files = new ArrayList<>();

        // try-with-resources; фильтр — только своя группа, иначе таймаут на тысячах файлов
        try (Stream<Path> stream = Files.walk(srcPath)) {
            stream.filter(p -> p.toString().endsWith(".java")
                            && p.toString().contains("group551003"))
                    .forEach(path -> {
                        try {
                            String content = readFile(path);
                            if (content == null) return; // тестовый — пропускаем

                            String processed = processContent(content);
                            files.add(new ProcessedFile(srcPath.relativize(path).toString(), processed));
                        } catch (IOException e) {
                            // MalformedInputException и прочее — файл пропускаем
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        findAndPrintCopies(files);
    }

    // Чтение построчно; IGNORE — игнорируем битые символы; @Test → null
    private static String readFile(Path path) throws IOException {
        StringBuilder sb = new StringBuilder();
        var decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(path), decoder))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("@Test") || line.contains("org.junit.Test")) return null;
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    // Порядок: package/import → комментарии → нормализация пробелов → trim
    private static String processContent(String content) {
        content = removePackageAndImports(content);
        content = removeComments(content);
        content = normalizeWhitespace(content);
        return content.trim();
    }

    // Удаление package и import
    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        for (String line : content.split("\n")) {
            String t = line.trim();
            if (!t.startsWith("package") && !t.startsWith("import")) result.append(line).append("\n");
        }
        return result.toString();
    }

    // Удаление комментариев за O(n)
    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        boolean inBlock = false, inLine = false, inStr = false, inChar = false;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (inLine) { if (c == '\n') { inLine = false; result.append(c); } continue; }
            if (inBlock) { if (c == '*' && i + 1 < content.length() && content.charAt(i + 1) == '/') { inBlock = false; i++; } continue; }
            if (inStr || inChar) {
                result.append(c);
                if (c == '\\' && i + 1 < content.length()) { result.append(content.charAt(++i)); }
                else if (inStr && c == '"') inStr = false;
                else if (inChar && c == '\'') inChar = false;
                continue;
            }
            if (c == '"') { inStr = true; result.append(c); }
            else if (c == '\'') { inChar = true; result.append(c); }
            else if (c == '/' && i + 1 < content.length()) {
                if (content.charAt(i + 1) == '/') { inLine = true; i++; }
                else if (content.charAt(i + 1) == '*') { inBlock = true; i++; }
                else result.append(c);
            } else result.append(c);
        }
        return result.toString();
    }

    // Все символы с кодом <33 → один пробел (серии схлопываем)
    private static String normalizeWhitespace(String text) {
        StringBuilder result = new StringBuilder();
        boolean lastSpace = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!lastSpace) { result.append(' '); lastSpace = true; }
            } else { result.append(c); lastSpace = false; }
        }
        return result.toString();
    }

    // Поиск копий: Левенштейн < 10; оптимизации — группировка по длине и порог в ДП
    private static void findAndPrintCopies(List<ProcessedFile> files) {
        Map<String, List<String>> copies = new TreeMap<>();

        Map<Integer, List<ProcessedFile>> byLength = new HashMap<>();
        for (ProcessedFile f : files) byLength.computeIfAbsent(f.content.length(), k -> new ArrayList<>()).add(f);

        for (ProcessedFile a : files) {
            if (copies.containsKey(a.path)) continue;
            List<String> found = new ArrayList<>();
            int len = a.content.length();
            // Порог 10 → разница длин <10, берём ±9
            for (int d = -9; d <= 9; d++) {
                List<ProcessedFile> cand = byLength.get(len + d);
                if (cand == null) continue;
                for (ProcessedFile b : cand) {
                    if (a == b || copies.containsKey(b.path)) continue;
                    if (optimizedLevenshtein(a.content, b.content, 10) < 10) found.add(b.path);
                }
            }
            if (!found.isEmpty()) { Collections.sort(found); copies.put(a.path, found); }
        }

        // Вывод: путь, затем пути копий; без пустых строк между группами
        for (Map.Entry<String, List<String>> e : copies.entrySet()) {
            System.out.println(e.getKey());
            for (String p : e.getValue()) System.out.println(p);
        }
    }

    // Левенштейн с ранним выходом; возвращает threshold+1, если расстояние ≥ threshold
    private static int optimizedLevenshtein(String s1, String s2, int threshold) {
        int m = s1.length(), n = s2.length();
        if (Math.abs(m - n) >= threshold) return threshold + 1;

        int[] prev = new int[n + 1], curr = new int[n + 1];
        for (int j = 0; j <= n; j++) prev[j] = j;

        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            int minRow = Integer.MAX_VALUE;
            for (int j = 1; j <= n; j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
                if (curr[j] < minRow) minRow = curr[j];
            }
            if (minRow >= threshold) return threshold + 1; // ранний выход
            int[] t = prev; prev = curr; curr = t;
        }
        return prev[n];
    }

    private static class ProcessedFile {
        private final String path;
        private final String content;
        ProcessedFile(String path, String content) { this.path = path; this.content = content; }
    }
}
