package by.it.group551001.maxim_belyak.lesson05;

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
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Разбиваем отрезки на два отсортированных массива начал и концов
        int[] starts = new int[n];
        int[] ends   = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i]   = segments[i].stop;
        }
        Arrays.sort(starts); // быстрая сортировка Arrays.sort = Dual-Pivot Quicksort
        Arrays.sort(ends);

        // Для каждой точки два бинарных поиска
        for (int i = 0; i < m; i++) {
            int p = points[i];

            int leftCount  = upperBound(starts, p);

            int rightCount = lowerBound(ends, p);
            result[i] = leftCount - rightCount;
        }

        return result;
    }

    // Количество элементов <= key
    private int upperBound(int[] arr, int key) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] <= key) lo = mid + 1;
            else                 hi = mid;
        }
        return lo;
    }

    // Количество элементов < key
    private int lowerBound(int[] arr, int key) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] < key) lo = mid + 1;
            else                hi = mid;
        }
        return lo;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // на случай если концы придут в обратном порядке
            this.start = Math.min(start, stop);
            this.stop  = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) return Integer.compare(this.start, o.start);
            return Integer.compare(this.stop, o.stop);
        }
    }
}