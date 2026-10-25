package by.it.group551001.chaley.lesson05;

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
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            segments[i] = new Segment(Math.min(a, b), Math.max(a, b));
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        Arrays.sort(segments);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int count = 0;

            int lastPossibleIdx = findLastPossible(segments, p);

            for (int j = 0; j <= lastPossibleIdx; j++) {
                if (segments[j].stop >= p) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    private int findLastPossible(Segment[] segments, int p) {
        int left = 0;
        int right = segments.length - 1;
        int ans = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= p) {
                ans = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
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
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}