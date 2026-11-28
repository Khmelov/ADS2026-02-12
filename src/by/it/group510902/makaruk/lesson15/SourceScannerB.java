package by.it.group510902.makaruk.lesson15;

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

public class SourceScannerB {

    private static class FileEntry {
        final int size;
        final String relPath;

        FileEntry(int size, String relPath) {
            this.size = size;
            this.relPath = relPath;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);
        if (!Files.isDirectory(srcPath)) {
            return;
        }

        List<Path> files = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(srcPath)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(files::add);
        } catch (IOException e) {
            return;
        }

        List<FileEntry> result = new ArrayList<>();
        for (Path file : files) {
            String content = readUtf8OrNull(file);
            if (content == null) {
                continue;
            }
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                continue;
            }

            String cleaned = removePackageAndImports(content);
            cleaned = removeCommentsLinear(cleaned);
            cleaned = trimBelow33(cleaned);
            cleaned = removeEmptyLinesLinear(cleaned);
            cleaned = trimBelow33(cleaned);

            int byteLength = cleaned.getBytes(StandardCharsets.UTF_8).length;
            String relative = srcPath.relativize(file).toString();
            result.add(new FileEntry(byteLength, relative));
        }

        result.sort(Comparator
                .comparingInt((FileEntry e) -> e.size)
                .thenComparing(e -> e.relPath));

        for (FileEntry entry : result) {
            System.out.println(entry.size + " " + entry.relPath);
        }
    }

    private static String readUtf8OrNull(Path file) {
        try {
            byte[] data = Files.readAllBytes(file);
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT);
            return decoder.decode(ByteBuffer.wrap(data)).toString();
        } catch (IOException e) {
            // Игнорируем файлы с MalformedInputException и другими ошибками чтения
            return null;
        }
    }

    private static String removePackageAndImports(String text) {
        int n = text.length();
        StringBuilder out = new StringBuilder(n);
        int i = 0;

        while (i < n) {
            int lineStart = i;
            int lineEnd = i;
            while (lineEnd < n && text.charAt(lineEnd) != '\n') {
                lineEnd++;
            }

            int p = lineStart;
            while (p < lineEnd && text.charAt(p) <= ' ') {
                p++;
            }

            boolean skip = startsWithKeyword(text, p, lineEnd, "package")
                    || startsWithKeyword(text, p, lineEnd, "import");

            if (!skip) {
                out.append(text, lineStart, lineEnd);
                if (lineEnd < n) {
                    out.append('\n');
                }
            }

            i = (lineEnd < n) ? lineEnd + 1 : n;
        }

        return out.toString();
    }

    private static boolean startsWithKeyword(String s, int start, int end, String kw) {
        int kwLen = kw.length();
        if (end - start < kwLen) {
            return false;
        }
        if (!s.startsWith(kw, start)) {
            return false;
        }
        if (start + kwLen == end) {
            return true;
        }
        return !Character.isJavaIdentifierPart(s.charAt(start + kwLen));
    }

    /**
     * Удаляет однострочные (//...) и многострочные (/*...* /) комментарии за O(n)
     * с учётом строковых и символьных литералов.
     */
    private static String removeCommentsLinear(String text) {
        int n = text.length();
        StringBuilder out = new StringBuilder(n);
        int i = 0;
        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;

        while (i < n) {
            char c = text.charAt(i);

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                    out.append(c);
                }
                i++;
                continue;
            }

            if (inBlockComment) {
                if (c == '*' && i + 1 < n && text.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            if (inString) {
                out.append(c);
                if (c == '\\' && i + 1 < n) {
                    out.append(text.charAt(i + 1));
                    i += 2;
                } else {
                    if (c == '"') {
                        inString = false;
                    }
                    i++;
                }
                continue;
            }

            if (inChar) {
                out.append(c);
                if (c == '\\' && i + 1 < n) {
                    out.append(text.charAt(i + 1));
                    i += 2;
                } else {
                    if (c == '\'') {
                        inChar = false;
                    }
                    i++;
                }
                continue;
            }

            if (c == '/' && i + 1 < n && text.charAt(i + 1) == '/') {
                inLineComment = true;
                i += 2;
            } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '*') {
                inBlockComment = true;
                i += 2;
            } else if (c == '"') {
                inString = true;
                out.append(c);
                i++;
            } else if (c == '\'') {
                inChar = true;
                out.append(c);
                i++;
            } else {
                out.append(c);
                i++;
            }
        }

        return out.toString();
    }

    private static String trimBelow33(String text) {
        int left = 0;
        int right = text.length();
        while (left < right && text.charAt(left) < 33) {
            left++;
        }
        while (right > left && text.charAt(right - 1) < 33) {
            right--;
        }
        return text.substring(left, right);
    }

    private static String removeEmptyLinesLinear(String text) {
        int n = text.length();
        StringBuilder out = new StringBuilder(n);
        int i = 0;

        while (i < n) {
            int lineStart = i;
            int lineEnd = i;
            while (lineEnd < n && text.charAt(lineEnd) != '\n') {
                lineEnd++;
            }

            boolean hasNonWhitespace = false;
            for (int p = lineStart; p < lineEnd; p++) {
                if (text.charAt(p) > ' ') {
                    hasNonWhitespace = true;
                    break;
                }
            }

            if (hasNonWhitespace) {
                out.append(text, lineStart, lineEnd);
                if (lineEnd < n) {
                    out.append('\n');
                }
            }

            i = (lineEnd < n) ? lineEnd + 1 : n;
        }

        return out.toString();
    }
}