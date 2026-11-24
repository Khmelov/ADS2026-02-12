package by.it.group551002.sevkovich.lesson15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;

public class SourceScannerB {
    private static ArrayList<Pair> files_list = new ArrayList<>();

    private static class Pair implements Comparable<Pair> {
        public Integer size;
        public String value;
        public Pair(String value, int size) {
            this.size = size;
            this.value = value;
        }

        @Override
        public int compareTo(Pair o) {
            return this.size.compareTo(o.size);
        }
        public String toString() {
            return new StringBuilder().append(this.size.toString()).
                append(' ').append(this.value).toString();
        }
    }
    public static void main(String[] args) {
        File src = new File(System.getProperty("user.dir"), "src");
        if (!src.isDirectory()) {
            return;
        }
        walkDirectory(src);
        files_list.sort(Comparator.naturalOrder());
        for (Pair pair : files_list) {
            System.out.println(pair);
        }
    }

    private static void walkDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                walkDirectory(file);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                files_list.add(new Pair(file.getPath(), formatFile(file)));
            }
        }
    }

    private static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            char[] buffer = new char[4096];
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, charsRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     *
     * @param file the file to format.
     * @return size of a formatted file.
     */
    private static int formatFile(File file) {
        String content = readFile(file);
        content = content.trim();
        StringBuilder sb = new StringBuilder();
        int src_i = 0;
        int len = content.length();
        while (src_i < len) {
            int line_start_i = src_i;
            while (src_i < len && content.charAt(src_i) != '\n') {
                src_i++;
            }
            int line_end_i = src_i;
            if (src_i < len && content.charAt(src_i) == '\n') {
                src_i++;
            }
            if (!isBlankRange(content, line_start_i, line_end_i) &&
                !isPackageOrImport(content, line_start_i, line_end_i)) {
                sb.append(content, line_start_i, line_end_i);
            }
        }
        return sb.length();
    }
    private static boolean isBlankRange(String text, int start, int end) {
        for (int i = start; i < end; i++) {
            if (text.charAt(i) >= 33) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPackageOrImport(String text, int start, int end) {
        int i = start;
        while (i < end && (text.charAt(i) == ' ' || text.charAt(i) == '\t')) {
            i++;
        }
        return startsWithKeyword(text, i, end, "package")
                || startsWithKeyword(text, i, end, "import");
    }
    private static boolean startsWithKeyword(String text, int offset, int end, String keyword) {
        int len = keyword.length();
        if (offset + len > end || !text.regionMatches(offset, keyword, 0, len)) {
            return false;
        }
        if (offset + len == end) {
            return true;
        }
        char next = text.charAt(offset + len);
        return next <= 32 || next == ';';
    }
}
