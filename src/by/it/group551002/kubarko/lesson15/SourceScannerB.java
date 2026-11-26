package by.it.group551002.kubarko.lesson15;

import java.io.File;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SourceScannerB {
    private record Record(int size, String path) {}

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path root = Path.of(src);
        List<Record> records;
        try (var walk = Files.walk(root)) {
            records = new ArrayList<>(walk.filter(p -> p.toString().endsWith(".java"))
                    .parallel()
                    .map(file -> {
                        try {
                            String text = readUtf8(file);
                            if (text.contains("@Test") || text.contains("org.junit.Test")) {
                                return null;
                            }
                            text = removePackageAndImports(text);
                            text = removeComments(text);
                            text = trimControlCharacters(text);
                            if (text.isEmpty()) {
                                return null;
                            }
                            return new Record(text.getBytes(StandardCharsets.UTF_8).length,
                                    root.relativize(file).toString());
                        } catch (java.io.IOException ignored) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList());
        } catch (java.io.IOException ignored) {
            return;
        }

        records.sort(Comparator.comparingInt(Record::size).thenComparing(Record::path));
        for (Record record : records) {
            System.out.println(record.size() + " " + record.path());
        }
    }

    private static String readUtf8(Path file) throws java.io.IOException, CharacterCodingException {
        byte[] bytes = Files.readAllBytes(file);
        java.nio.charset.CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(java.nio.charset.CodingErrorAction.REPORT)
                .onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPORT);
        return decoder.decode(java.nio.ByteBuffer.wrap(bytes)).toString();
    }

    private static String removePackageAndImports(String text) {
        String[] lines = text.split("\\R");
        StringBuilder result = new StringBuilder(text.length());
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                result.append(line).append('\n');
            }
        }
        return result.toString();
    }

    private static String trimControlCharacters(String text) {
        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) {
            start++;
        }
        int end = text.length();
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }
        return text.substring(start, end);
    }

    private static String removeComments(String text) {
        StringBuilder result = new StringBuilder(text.length());
        boolean block = false;
        boolean line = false;
        boolean string = false;
        boolean character = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            char next = i + 1 < text.length() ? text.charAt(i + 1) : 0;

            if (line) {
                if (c == '\n' || c == '\r') {
                    line = false;
                    result.append(c);
                }
            } else if (block) {
                if (c == '*' && next == '/') {
                    block = false;
                    i++;
                }
            } else if (string) {
                result.append(c);
                if (c == '\\' && next != 0) {
                    result.append(next);
                    i++;
                } else if (c == '"') {
                    string = false;
                }
            } else if (character) {
                result.append(c);
                if (c == '\\' && next != 0) {
                    result.append(next);
                    i++;
                } else if (c == '\'') {
                    character = false;
                }
            } else if (c == '/' && next == '/') {
                line = true;
                i++;
            } else if (c == '/' && next == '*') {
                block = true;
                i++;
            } else {
                result.append(c);
                if (c == '"') string = true;
                else if (c == '\'') character = true;
            }
        }
        return result.toString();
    }
}
