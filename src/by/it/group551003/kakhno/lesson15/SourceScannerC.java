
package by.it.group551003.kakhno.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SourceScannerC {

    static class Source {
        String path;
        String text;

        Source(String path, String text) {
            this.path = path;
            this.text = text;
        }
    }

    public static void main(String[] args) throws IOException {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        Path root = Path.of(src);
        List<Source> sources = new ArrayList<>();

        try (var walk = Files.walk(root)) {
            walk.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            String text = Files.readString(
                                    p, StandardCharsets.UTF_8
                            );

                            if (!text.contains("@Test")
                                    && !text.contains("org.junit.Test")) {
                                text = removePackageAndImports(text);
                                text = removeComments(text);
                                text = normalize(text);

                                String relativePath = root.relativize(p)
                                        .toString()
                                        .replace(File.separatorChar, '/');

                                sources.add(new Source(relativePath, text));
                            }
                        } catch (MalformedInputException e) {
                        } catch (IOException e) {
                        }
                    });
        }

        Collections.sort(sources,
                (a, b) -> a.path.compareTo(b.path));

        List<List<String>> copies = new ArrayList<>();

        for (int i = 0; i < sources.size(); i++)
            copies.add(new ArrayList<>());

        for (int i = 0; i < sources.size(); i++) {
            Source a = sources.get(i);

            for (int j = i + 1; j < sources.size(); j++) {
                Source b = sources.get(j);

                if (Math.abs(a.text.length() - b.text.length()) < 10) {
                    int distance = levenshtein(a.text, b.text);

                    if (distance < 10) {
                        copies.get(i).add(b.path);
                        copies.get(j).add(a.path);
                    }
                }
            }
        }

        boolean[] printed = new boolean[sources.size()];

        for (int i = 0; i < sources.size(); i++) {
            if (!copies.get(i).isEmpty() && !printed[i]) {
                List<String> group = new ArrayList<>();
                group.add(sources.get(i).path);
                printed[i] = true;

                for (String copy : copies.get(i)) {
                    group.add(copy);
                    for (int k = 0; k < sources.size(); k++)
                        if (sources.get(k).path.equals(copy)) {
                            printed[k] = true;
                            break;
                        }
                }

                Collections.sort(group);

                for (String file : group)
                    System.out.println(file);

                System.out.println();
            }
        }
    }

    private static String removePackageAndImports(String text) {
        return text.replaceAll(
                "(?m)^\\s*(package|import)\\s+[^;]*;\\s*",
                ""
        );
    }

    private static String removeComments(String text) {
        StringBuilder result = new StringBuilder();

        int i = 0;
        int n = text.length();

        boolean string = false;
        boolean character = false;
        boolean lineComment = false;
        boolean blockComment = false;
        boolean escape = false;

        while (i < n) {
            char c = text.charAt(i);
            char next = i + 1 < n ? text.charAt(i + 1) : 0;

            if (lineComment) {
                if (c == '\n') {
                    lineComment = false;
                    result.append(c);
                }
            } else if (blockComment) {
                if (c == '*' && next == '/') {
                    blockComment = false;
                    i++;
                }
            } else if (string) {
                result.append(c);

                if (escape)
                    escape = false;
                else if (c == '\\')
                    escape = true;
                else if (c == '"')
                    string = false;
            } else if (character) {
                result.append(c);

                if (escape)
                    escape = false;
                else if (c == '\\')
                    escape = true;
                else if (c == '\'')
                    character = false;
            } else {
                if (c == '/' && next == '/') {
                    lineComment = true;
                    i++;
                } else if (c == '/' && next == '*') {
                    blockComment = true;
                    i++;
                } else if (c == '"') {
                    string = true;
                    result.append(c);
                } else if (c == '\'') {
                    character = true;
                    result.append(c);
                } else
                    result.append(c);
            }

            i++;
        }

        return result.toString();
    }

    private static String normalize(String text) {
        StringBuilder result = new StringBuilder();
        boolean space = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c < 33) {
                if (!space) {
                    result.append(' ');
                    space = true;
                }
            } else {
                result.append(c);
                space = false;
            }
        }

        return result.toString().trim();
    }

    private static int levenshtein(String a, String b) {
        int n = a.length();
        int m = b.length();

        if (Math.abs(n - m) >= 10)
            return 10;

        int start = 0;

        while (start < n && start < m
                && a.charAt(start) == b.charAt(start))
            start++;

        int endA = n;
        int endB = m;

        while (endA > start && endB > start
                && a.charAt(endA - 1) == b.charAt(endB - 1)) {
            endA--;
            endB--;
        }

        a = a.substring(start, endA);
        b = b.substring(start, endB);

        n = a.length();
        m = b.length();

        if (m > n) {
            String temp = a;
            a = b;
            b = temp;

            int t = n;
            n = m;
            m = t;
        }

        int[] previous = new int[m + 1];
        int[] current = new int[m + 1];

        for (int j = 0; j <= Math.min(m, 9); j++)
            previous[j] = j;

        if (m >= 10)
            previous[10] = 10;

        for (int i = 1; i <= n; i++) {
            int left = Math.max(1, i - 9);
            int right = Math.min(m, i + 9);

            if (left == 1)
                current[0] = i;
            else
                current[left - 1] = 10;

            if (right < m)
                current[right + 1] = 10;

            for (int j = left; j <= right; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1)
                        ? 0 : 1;

                current[j] = Math.min(
                        Math.min(
                                previous[j] + 1,
                                current[j - 1] + 1
                        ),
                        previous[j - 1] + cost
                );

                if (current[j] > 10)
                    current[j] = 10;
            }

            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return previous[m];
    }
}
