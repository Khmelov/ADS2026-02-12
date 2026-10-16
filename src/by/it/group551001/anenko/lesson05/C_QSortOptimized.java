package by.it.group551001.anenko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        if (!scanner.hasNextInt()) return new int[0];

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        int[] starts = new int[n];
        int[] stops = new int[n];

        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            starts[i] = Math.min(a, b);
            stops[i] = Math.max(a, b);
        }

        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        qsort(starts, 0, n - 1);
        qsort(stops, 0, n - 1);

        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int sCount = upper_bound(starts, p);
            int eCount = lower_bound(stops, p);
            result[i] = sCount - eCount;
        }

        return result;
    }

    private void qsort(int[] a, int left, int right) {
        while (left < right) {
            int pivot = a[left + (right - left) / 2];
            int lt = left;
            int gt = right;
            int i = left;

            while (i <= gt) {
                if (a[i] < pivot) {
                    swap(a, lt++, i++);
                } else if (a[i] > pivot) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }

            if (lt - left < right - gt) {
                qsort(a, left, lt - 1);
                left = gt + 1;
            } else {
                qsort(a, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private int upper_bound(int[] a, int key) {
        int l = 0, r = a.length;
        while (l < r) {
            int m = (l + r) / 2;
            if (a[m] <= key) l = m + 1;
            else r = m;
        }
        return l;
    }

    private int lower_bound(int[] a, int key) {
        int l = 0, r = a.length;
        while (l < r) {
            int m = (l + r) / 2;
            if (a[m] < key) l = m + 1;
            else r = m;
        }
        return l;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
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