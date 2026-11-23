package by.it.group551001.Barusevich.lesson15;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
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

final class SourceScannerUtils {
    private SourceScannerUtils() {
    }

    static final class SourceFile {
        final String path;
        final String text;

        SourceFile(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }

    static Path sourceRoot() {
        return Paths.get(System.getProperty("user.dir"), "src");
    }

    static List<SourceFile> readJavaFiles() throws IOException {
        Path root = sourceRoot();
        List<Path> paths = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(root)) {
            walk.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".java"))
                    .forEach(paths::add);
        }

        paths.sort(Comparator.comparing(path -> root.relativize(path).toString()));

        List<SourceFile> result = new ArrayList<>();
        for (Path path : paths) {
            try {
                String text = readText(path);
                if (!isTest(text)) {
                    result.add(new SourceFile(root.relativize(path).toString(), text));
                }
            } catch (IOException ignored) {
                // Недоступный файл не может участвовать в обработке.
            }
        }
        return result;
    }

    private static String readText(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.IGNORE)
                .onUnmappableCharacter(CodingErrorAction.IGNORE);
        try {
            return decoder.decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException exception) {
            // При IGNORE декодер обычно не выбрасывает это исключение.
            return "";
        }
    }

    private static boolean isTest(String text) {
        return text.contains("@Test") || text.contains("org.junit.Test");
    }

    static String removePackageAndImports(String text) {
        StringBuilder result = new StringBuilder(text.length());
        int length = text.length();
        int lineStart = 0;

        while (lineStart < length) {
            int lineEnd = lineStart;
            while (lineEnd < length) {
                char c = text.charAt(lineEnd);
                if (c == '\n' || c == '\r') {
                    break;
                }
                lineEnd++;
            }

            int afterLine = lineEnd;
            if (afterLine < length && text.charAt(afterLine) == '\r') {
                afterLine++;
            }
            if (afterLine < length && text.charAt(afterLine) == '\n') {
                afterLine++;
            }

            boolean directive = isDirective(text, lineStart, lineEnd, "package")
                    || isDirective(text, lineStart, lineEnd, "import");
            if (!directive) {
                result.append(text, lineStart, afterLine);
            }
            lineStart = afterLine;
        }
        return result.toString();
    }

    private static boolean isDirective(String text, int start, int end, String keyword) {
        while (start < end && text.charAt(start) <= 32) {
            start++;
        }
        int keywordEnd = start + keyword.length();
        if (keywordEnd > end || !text.regionMatches(start, keyword, 0, keyword.length())) {
            return false;
        }
        return keywordEnd == end || text.charAt(keywordEnd) <= 32 || text.charAt(keywordEnd) == ';';
    }

    static String trimControlCharacters(String text) {
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

    static String removeComments(String text) {
        StringBuilder result = new StringBuilder(text.length());
        int i = 0;
        int length = text.length();

        while (i < length) {
            char c = text.charAt(i);

            if (c == '/' && i + 1 < length && text.charAt(i + 1) == '/') {
                result.append(' ');
                i += 2;
                while (i < length && text.charAt(i) != '\n' && text.charAt(i) != '\r') {
                    i++;
                }
            } else if (c == '/' && i + 1 < length && text.charAt(i + 1) == '*') {
                result.append(' ');
                i += 2;
                while (i < length) {
                    if (text.charAt(i) == '*' && i + 1 < length && text.charAt(i + 1) == '/') {
                        i += 2;
                        break;
                    }
                    char inside = text.charAt(i++);
                    if (inside == '\n' || inside == '\r') {
                        result.append(inside);
                    }
                }
            } else if (c == '"' && i + 2 < length
                    && text.charAt(i + 1) == '"' && text.charAt(i + 2) == '"') {
                result.append("\"\"\"");
                i += 3;
                while (i < length) {
                    char current = text.charAt(i++);
                    result.append(current);
                    if (current == '\\' && i < length) {
                        result.append(text.charAt(i++));
                    } else if (current == '"' && i + 1 < length
                            && text.charAt(i) == '"' && text.charAt(i + 1) == '"') {
                        result.append("\"\"");
                        i += 2;
                        break;
                    }
                }
            } else if (c == '"' || c == '\'') {
                char quote = c;
                result.append(c);
                i++;
                while (i < length) {
                    char current = text.charAt(i++);
                    result.append(current);
                    if (current == '\\' && i < length) {
                        result.append(text.charAt(i++));
                    } else if (current == quote) {
                        break;
                    }
                }
            } else {
                result.append(c);
                i++;
            }
        }
        return result.toString();
    }

    static String removeEmptyLines(String text) {
        StringBuilder result = new StringBuilder(text.length());
        int length = text.length();
        int lineStart = 0;

        while (lineStart < length) {
            int lineEnd = lineStart;
            while (lineEnd < length && text.charAt(lineEnd) != '\n' && text.charAt(lineEnd) != '\r') {
                lineEnd++;
            }

            int afterLine = lineEnd;
            if (afterLine < length && text.charAt(afterLine) == '\r') {
                afterLine++;
            }
            if (afterLine < length && text.charAt(afterLine) == '\n') {
                afterLine++;
            }

            boolean hasContent = false;
            for (int i = lineStart; i < lineEnd; i++) {
                if (text.charAt(i) > 32) {
                    hasContent = true;
                    break;
                }
            }
            if (hasContent) {
                result.append(text, lineStart, afterLine);
            }
            lineStart = afterLine;
        }
        return result.toString();
    }

    static String flattenWhitespace(String text) {
        StringBuilder result = new StringBuilder(text.length());
        boolean inWhitespace = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!inWhitespace) {
                    result.append(' ');
                    inWhitespace = true;
                }
            } else {
                result.append(c);
                inWhitespace = false;
            }
        }
        return trimControlCharacters(result.toString());
    }
}
