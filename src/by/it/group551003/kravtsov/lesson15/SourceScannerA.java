package by.it.group551003.kravtsov.lesson15;

import java.io.File;
import java.io.IOException;
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
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);
        List<FileInfo> fileInfos = new ArrayList<>();

        // try-with-resources: поток Files.walk обязательно закрываем
        try (Stream<Path> stream = Files.walk(srcPath)) {
            stream.filter(p -> p.toString().endsWith(".java")).forEach(path -> {
                try {
                    String content = readFiltered(path);
                    if (content == null) return; // тестовый файл — пропускаем

                    String processed = trimControlChars(content);
                    int size = processed.getBytes(StandardCharsets.UTF_8).length;
                    fileInfos.add(new FileInfo(srcPath.relativize(path).toString(), size));
                } catch (IOException e) {
                    // MalformedInputException и прочие ошибки чтения — просто игнорируем файл
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Сортировка: по размеру, при равенстве — лексикографически по пути
        fileInfos.stream()
                .sorted(Comparator.comparingInt(FileInfo::getSize).thenComparing(FileInfo::getPath))
                .forEach(i -> System.out.println(i.getSize() + " " + i.getPath()));
    }

    // Читаем построчно; package/import отбрасываем; при @Test возвращаем null
    private static String readFiltered(Path path) throws IOException {
        StringBuilder sb = new StringBuilder();
        // IGNORE — «способ игнорировать ошибки MalformedInputException» из условия
        var decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);
        try (var br = new java.io.BufferedReader(
                new java.io.InputStreamReader(Files.newInputStream(path), decoder))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("@Test") || line.contains("org.junit.Test")) return null;
                String t = line.trim();
                if (!t.startsWith("package") && !t.startsWith("import")) sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }
    // Удаляем символы с кодом <33 в начале и конце
    private static String trimControlChars(String text) {
        int start = 0, end = text.length();
        while (start < end && text.charAt(start) < 33) start++;
        while (end > start && text.charAt(end - 1) < 33) end--;
        return text.substring(start, end);
    }

    private static class FileInfo {
        private final String path;
        private final int size;
        FileInfo(String path, int size) { this.path = path; this.size = size; }
        public String getPath() { return path; }
        public int getSize() { return size; }
    }
}