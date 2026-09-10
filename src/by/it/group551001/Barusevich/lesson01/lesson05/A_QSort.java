package by.it.group551001.Barusevich.lesson01.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            segments[i] = new Segment(start, stop);
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        Arrays.sort(segments);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            int left = 0;
            int right = n - 1;
            int firstIndex = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    firstIndex = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            for (int j = 0; j <= firstIndex; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}