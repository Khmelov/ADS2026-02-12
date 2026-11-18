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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class SourceScannerB {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);
        List<FileInfo> fileInfos = new ArrayList<>();

        // try-with-resources: закрываем поток обхода
        try (Stream<Path> stream = Files.walk(srcPath)) {
            stream.filter(p -> p.toString().endsWith(".java")).forEach(path -> {
                try {
                    String content = readFile(path);
                    if (content == null) return; // тестовый — пропускаем

                    String processed = processContent(content);
                    int size = processed.getBytes(StandardCharsets.UTF_8).length;
                    fileInfos.add(new FileInfo(srcPath.relativize(path).toString(), size));
                } catch (IOException e) {
                    // MalformedInputException и прочее — файл пропускаем
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка: по размеру, при равенстве — по пути
        fileInfos.stream()
                .sorted(Comparator.comparingInt(FileInfo::getSize).thenComparing(FileInfo::getPath))
                .forEach(i -> System.out.println(i.getSize() + " " + i.getPath()));
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

    // Порядок важен: сначала package/import, потом комментарии, потом trim и пустые строки
    private static String processContent(String content) {
        content = removePackageAndImports(content);
        content = removeComments(content);
        content = trimLowChars(content);
        return removeEmptyLines(content);
    }

    // Удаление строк package и import
    private static String removePackageAndImports(String content) {
        StringBuilder result = new StringBuilder();
        for (String line : content.split("\n")) {
            String t = line.trim();
            if (!t.startsWith("package") && !t.startsWith("import")) result.append(line).append("\n");
        }
        return result.toString();
    }

    // Удаление комментариев за O(n): учитываем строки, символы и экранирование
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

    // Обрезка символов с кодом <33 по краям
    private static String trimLowChars(String text) {
        int start = 0, end = text.length();
        while (start < end && text.charAt(start) < 33) start++;
        while (end > start && text.charAt(end - 1) < 33) end--;
        return text.substring(start, end);
    }
    // Удаление пустых строк
    private static String removeEmptyLines(String content) {
        StringBuilder result = new StringBuilder();
        for (String line : content.split("\n")) {
            if (!line.trim().isEmpty()) result.append(line).append("\n");
        }
        return result.toString();
    }

    private static class FileInfo {
        private final String path;
        private final int size;
        FileInfo(String path, int size) { this.path = path; this.size = size; }
        public String getPath() { return path; }
        public int getSize() { return size; }
    }
}