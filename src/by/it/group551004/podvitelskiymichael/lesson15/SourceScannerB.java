package by.it.group551004.podvitelskiymichael.lesson15;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerB {

    private static final Charset CP1251 = Charset.forName("windows-1251");

    private record FileRecord(int size, String path) {
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;
        Path root = Path.of(src);

        List<FileRecord> records;
        try (Stream<Path> walk = Files.walk(root)) {
            List<Path> files = walk
                    .filter(p -> p.toString().endsWith(".java"))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            records = files.parallelStream()
                    .map(p -> process(root, p))
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(FileRecord::size)
                            .thenComparing(FileRecord::path))
                    .collect(Collectors.toList());
        } catch (IOException | UncheckedIOException e) {
            return;
        }

        StringBuilder out = new StringBuilder();
        for (FileRecord r : records) {
            out.append(r.size()).append(' ').append(r.path()).append('\n');
        }
        System.out.print(out);
        System.out.flush();
    }

    private static FileRecord process(Path root, Path p) {
        try {
            String raw = decode(Files.readAllBytes(p));
            if (raw.contains("@Test") || raw.contains("org.junit.Test")) {
                return null;
            }
            String text = trimEnds(removeBlankLines(stripComments(removeHeader(raw))));
            int size = text.getBytes(StandardCharsets.UTF_8).length;
            return new FileRecord(size, root.relativize(p).toString());
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private static String decode(byte[] bytes) {
        String s;
        try {
            s = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes))
                    .toString();
        } catch (CharacterCodingException e) {
            s = new String(bytes, CP1251);
        }
        if (!s.isEmpty() && s.charAt(0) == '\uFEFF') {
            s = s.substring(1);
        }
        return s;
    }

    private static String removeHeader(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n);
        int pos = 0;
        while (pos < n) {
            int nl = s.indexOf('\n', pos);
            int end = nl < 0 ? n : nl;
            int lineEnd = end;
            if (lineEnd > pos && s.charAt(lineEnd - 1) == '\r') {
                lineEnd--;
            }
            int k = pos;
            while (k < lineEnd && s.charAt(k) <= ' ') {
                k++;
            }
            if (!isKeyword(s, k, lineEnd, "package") && !isKeyword(s, k, lineEnd, "import")) {
                sb.append(s, pos, lineEnd).append('\n');
            }
            pos = end + 1;
        }
        return sb.toString();
    }

    private static boolean isKeyword(String s, int from, int end, String kw) {
        int len = kw.length();
        return from + len < end && s.startsWith(kw, from) && s.charAt(from + len) <= ' ';
    }

    private static String stripComments(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n);
        int i = 0;
        while (i < n) {
            char c = s.charAt(i);
            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '/') {
                i += 2;
                while (i < n && s.charAt(i) != '\n') {
                    i++;
                }
            } else if (c == '/' && i + 1 < n && s.charAt(i + 1) == '*') {
                i += 2;
                while (i < n && !(s.charAt(i) == '*' && i + 1 < n && s.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2;
            } else if (c == '"' && s.startsWith("\"\"\"", i)) {
                sb.append("\"\"\"");
                i += 3;
                while (i < n && !s.startsWith("\"\"\"", i)) {
                    if (s.charAt(i) == '\\' && i + 1 < n) {
                        sb.append(s.charAt(i++));
                    }
                    sb.append(s.charAt(i++));
                }
                if (i < n) {
                    sb.append("\"\"\"");
                    i += 3;
                }
            } else if (c == '"' || c == '\'') {
                sb.append(c);
                i++;
                while (i < n) {
                    char d = s.charAt(i);
                    if (d == '\\' && i + 1 < n) {
                        sb.append(d).append(s.charAt(i + 1));
                        i += 2;
                        continue;
                    }
                    if (d == '\n') {
                        break;
                    }
                    sb.append(d);
                    i++;
                    if (d == c) {
                        break;
                    }
                }
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    private static String removeBlankLines(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n);
        int pos = 0;
        while (pos < n) {
            int nl = s.indexOf('\n', pos);
            int end = nl < 0 ? n : nl;
            boolean blank = true;
            for (int k = pos; k < end; k++) {
                if (s.charAt(k) > ' ') {
                    blank = false;
                    break;
                }
            }
            if (!blank) {
                sb.append(s, pos, end).append('\n');
            }
            pos = end + 1;
        }
        return sb.toString();
    }

    private static String trimEnds(String s) {
        int start = 0;
        int end = s.length();
        while (start < end && s.charAt(start) < 33) {
            start++;
        }
        while (end > start && s.charAt(end - 1) < 33) {
            end--;
        }
        return s.substring(start, end);
    }
}