package by.it.group551001.Barusevich.lesson15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SourceScannerC {
    private static final int COPY_DISTANCE_LIMIT = 9;

    public static void main(String[] args) throws IOException {
        List<SourceScannerUtils.SourceFile> files = new ArrayList<>();

        for (SourceScannerUtils.SourceFile file : SourceScannerUtils.readJavaFiles()) {
            String text = SourceScannerUtils.removePackageAndImports(file.text);
            text = SourceScannerUtils.removeComments(text);
            text = SourceScannerUtils.flattenWhitespace(text);
            files.add(new SourceScannerUtils.SourceFile(file.path, text));
        }

        List<List<Integer>> copies = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            copies.add(new ArrayList<>());
        }

        for (int i = 0; i < files.size(); i++) {
            for (int j = i + 1; j < files.size(); j++) {
                if (distanceAtMost(files.get(i).text, files.get(j).text, COPY_DISTANCE_LIMIT)) {
                    copies.get(i).add(j);
                    copies.get(j).add(i);
                }
            }
        }

        for (int i = 0; i < files.size(); i++) {
            if (copies.get(i).isEmpty()) {
                continue;
            }
            System.out.println(files.get(i).path);
            for (int copyIndex : copies.get(i)) {
                System.out.println(files.get(copyIndex).path);
            }
            System.out.println();
        }
    }

    // Возвращает true, если расстояние Левенштейна не превышает limit.
    // Используется узкая диагональная полоса: O(limit * max(n, m)) времени и O(min(n, m)) памяти.
    private static boolean distanceAtMost(String first, String second, int limit) {
        int firstStart = 0;
        int secondStart = 0;
        int firstEnd = first.length();
        int secondEnd = second.length();

        while (firstStart < firstEnd && secondStart < secondEnd
                && first.charAt(firstStart) == second.charAt(secondStart)) {
            firstStart++;
            secondStart++;
        }
        while (firstStart < firstEnd && secondStart < secondEnd
                && first.charAt(firstEnd - 1) == second.charAt(secondEnd - 1)) {
            firstEnd--;
            secondEnd--;
        }

        int firstLength = firstEnd - firstStart;
        int secondLength = secondEnd - secondStart;
        if (Math.abs(firstLength - secondLength) > limit) {
            return false;
        }
        if (firstLength == 0) {
            return secondLength <= limit;
        }
        if (secondLength == 0) {
            return firstLength <= limit;
        }

        if (firstLength < secondLength) {
            String tempString = first;
            first = second;
            second = tempString;

            int tempStart = firstStart;
            firstStart = secondStart;
            secondStart = tempStart;

            int tempEnd = firstEnd;
            firstEnd = secondEnd;
            secondEnd = tempEnd;

            int tempLength = firstLength;
            firstLength = secondLength;
            secondLength = tempLength;
        }

        int n = firstLength;
        int m = secondLength;
        int infinity = limit + 1;
        int[] previous = new int[m + 1];
        int[] current = new int[m + 1];

        int previousStart = 0;
        int previousEnd = Math.min(m, limit);
        for (int j = 0; j <= previousEnd; j++) {
            previous[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            int currentStart = Math.max(0, i - limit);
            int currentEnd = Math.min(m, i + limit);
            int rowMinimum = infinity;

            if (currentStart == 0) {
                current[0] = i;
                rowMinimum = i;
            }

            int from = Math.max(1, currentStart);
            for (int j = from; j <= currentEnd; j++) {
                int deletion = (j >= previousStart && j <= previousEnd)
                        ? previous[j] + 1 : infinity;
                int insertion = (j - 1 >= currentStart)
                        ? current[j - 1] + 1 : infinity;
                int substitution = (j - 1 >= previousStart && j - 1 <= previousEnd)
                        ? previous[j - 1]
                        + (first.charAt(firstStart + i - 1) == second.charAt(secondStart + j - 1) ? 0 : 1)
                        : infinity;

                int value = Math.min(Math.min(deletion, insertion), substitution);
                current[j] = value;
                if (value < rowMinimum) {
                    rowMinimum = value;
                }
            }

            if (rowMinimum > limit) {
                return false;
            }

            int[] temp = previous;
            previous = current;
            current = temp;
            previousStart = currentStart;
            previousEnd = currentEnd;
        }

        return m >= previousStart && m <= previousEnd && previous[m] <= limit;
    }
}
