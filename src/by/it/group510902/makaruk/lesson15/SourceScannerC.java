package by.it.group510902.makaruk.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
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

public class SourceScannerC {

    private static class FileEntry {
        final String path;
        final char[] content;

        FileEntry(String path, char[] content) {
            this.path = path;
            this.content = content;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        if (!Files.exists(srcPath)) {
            return;
        }

        List<FileEntry> files = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(srcPath)) {
            walk.filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".java"))
                    .forEach(p -> {
                        String rawContent = readFileSafely(p);
                        if (rawContent != null && !rawContent.contains("@Test") && !rawContent.contains("org.junit.Test")) {
                            char[] processed = processText(rawContent);
                            files.add(new FileEntry(p.toString(), processed));
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        files.sort(Comparator.comparing(f -> f.path));

        int size = files.size();
        for (int i = 0; i < size; i++) {
            FileEntry current = files.get(i);
            List<String> copies = new ArrayList<>();

            for (int j = 0; j < size; j++) {
                if (i == j) continue;
                FileEntry candidate = files.get(j);

                if (Math.abs(current.content.length - candidate.content.length) >= 10) {
                    continue;
                }

                if (levenshteinLessThan10(current.content, candidate.content)) {
                    copies.add(candidate.path);
                }
            }

            if (!copies.isEmpty()) {
                System.out.println(current.path);
                for (String copyPath : copies) {
                    System.out.println(copyPath);
                }
            }
        }
    }

    private static String readFileSafely(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPLACE)
                    .onUnmappableCharacter(CodingErrorAction.REPLACE);
            CharBuffer decoded = decoder.decode(ByteBuffer.wrap(bytes));
            return decoded.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private static char[] processText(String text) {
        int n = text.length();
        char[] out = new char[n];
        int outLen = 0;

        int i = 0;
        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;
        boolean lastWasSpace = true;

        while (i < n) {
            char c = text.charAt(i);
            char next = (i + 1 < n) ? text.charAt(i + 1) : '\0';

            if (inLineComment) {
                if (c == '\n' || c == '\r') {
                    inLineComment = false;
                    if (!lastWasSpace) {
                        out[outLen++] = ' ';
                        lastWasSpace = true;
                    }
                }
                i++;
                continue;
            }

            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i += 2;
                    if (!lastWasSpace) {
                        out[outLen++] = ' ';
                        lastWasSpace = true;
                    }
                } else {
                    i++;
                }
                continue;
            }

            if (inString) {
                if (c == '\\' && i + 1 < n) {
                    outLen = appendChar(out, outLen, c, lastWasSpace);
                    lastWasSpace = (c < 33);
                    outLen = appendChar(out, outLen, next, lastWasSpace);
                    lastWasSpace = (next < 33);
                    i += 2;
                    continue;
                }
                if (c == '"') {
                    inString = false;
                }
                outLen = appendChar(out, outLen, c, lastWasSpace);
                lastWasSpace = (c < 33);
                i++;
                continue;
            }

            if (inChar) {
                if (c == '\\' && i + 1 < n) {
                    outLen = appendChar(out, outLen, c, lastWasSpace);
                    lastWasSpace = (c < 33);
                    outLen = appendChar(out, outLen, next, lastWasSpace);
                    lastWasSpace = (next < 33);
                    i += 2;
                    continue;
                }
                if (c == '\'') {
                    inChar = false;
                }
                outLen = appendChar(out, outLen, c, lastWasSpace);
                lastWasSpace = (c < 33);
                i++;
                continue;
            }

            if (c == '/' && next == '/') {
                inLineComment = true;
                i += 2;
                continue;
            }
            if (c == '/' && next == '*') {
                inBlockComment = true;
                i += 2;
                continue;
            }

            if (c == '"') {
                inString = true;
                outLen = appendChar(out, outLen, c, lastWasSpace);
                lastWasSpace = false;
                i++;
                continue;
            }
            if (c == '\'') {
                inChar = true;
                outLen = appendChar(out, outLen, c, lastWasSpace);
                lastWasSpace = false;
                i++;
                continue;
            }

            if (lastWasSpace) {
                if (startsWithKeyword(text, i, "package")) {
                    i = skipUntilSemicolon(text, i + 7);
                    continue;
                }
                if (startsWithKeyword(text, i, "import")) {
                    i = skipUntilSemicolon(text, i + 6);
                    continue;
                }
            }

            if (c < 33) {
                if (!lastWasSpace) {
                    out[outLen++] = ' ';
                    lastWasSpace = true;
                }
            } else {
                out[outLen++] = c;
                lastWasSpace = false;
            }
            i++;
        }

        if (outLen > 0 && out[outLen - 1] == ' ') {
            outLen--;
        }

        char[] result = new char[outLen];
        System.arraycopy(out, 0, result, 0, outLen);
        return result;
    }

    private static int appendChar(char[] out, int outLen, char c, boolean lastWasSpace) {
        if (c < 33) {
            if (!lastWasSpace) {
                out[outLen++] = ' ';
            }
        } else {
            out[outLen++] = c;
        }
        return outLen;
    }

    private static boolean startsWithKeyword(String text, int index, String keyword) {
        int kwLen = keyword.length();
        if (index + kwLen >= text.length()) return false;
        if (!text.startsWith(keyword, index)) return false;
        char after = text.charAt(index + kwLen);
        return after < 33 || after == '/';
    }

    private static int skipUntilSemicolon(String text, int index) {
        int n = text.length();
        while (index < n && text.charAt(index) != ';') {
            index++;
        }
        return (index < n) ? index + 1 : n;
    }

    private static boolean levenshteinLessThan10(char[] s1, char[] s2) {
        if (s1.length > s2.length) {
            char[] temp = s1;
            s1 = s2;
            s2 = temp;
        }

        int len1 = s1.length;
        int len2 = s2.length;
        final int MAX_DIST = 9;

        int[] prev = new int[len1 + 1];
        int[] curr = new int[len1 + 1];

        for (int i = 0; i <= len1; i++) {
            prev[i] = i;
        }

        for (int j = 1; j <= len2; j++) {
            curr[0] = j;
            int minInRow = curr[0];

            int from = Math.max(1, j - MAX_DIST);
            int to = Math.min(len1, j + MAX_DIST);

            if (from > 1) {
                curr[from - 1] = MAX_DIST + 1;
            }

            for (int i = from; i <= to; i++) {
                int cost = (s1[i - 1] == s2[j - 1]) ? 0 : 1;
                int val = Math.min(
                        Math.min(curr[i - 1] + 1, prev[i] + 1),
                        prev[i - 1] + cost
                );
                curr[i] = val;
                if (val < minInRow) {
                    minInRow = val;
                }
            }

            if (to < len1) {
                curr[to + 1] = MAX_DIST + 1;
            }

            if (minInRow > MAX_DIST) {
                return false;
            }

            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len1] <= MAX_DIST;
    }
}
