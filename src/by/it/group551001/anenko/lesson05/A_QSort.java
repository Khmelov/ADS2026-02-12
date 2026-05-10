package by.it.group551001.anenko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
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
        if (!scanner.hasNextInt()) return new int[0];

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        int[] starts = new int[n];
        int[] stops = new int[n];
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            starts[i] = Math.min(a, b);
            stops[i] = Math.max(a, b);
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        quickSort(starts, 0, n - 1);
        quickSort(stops, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int started = countLessOrEqual(starts, p);
            int ended = countLess(stops, p);
            result[i] = started - ended;
        }

        return result;
    }

    private void quickSort(int[] a, int left, int right) {
        if (left >= right) return;
        int i = left, j = right;
        int pivot = a[left + (right - left) / 2];
        while (i <= j) {
            while (a[i] < pivot) i++;
            while (a[j] > pivot) j--;
            if (i <= j) {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                i++;
                j--;
            }
        }
        quickSort(a, left, j);
        quickSort(a, i, right);
    }

    private int countLessOrEqual(int[] arr, int x) {
        int low = 0, high = arr.length - 1;
        int count = 0;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (arr[mid] <= x) {
                count = mid + 1;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return count;
    }

    private int countLess(int[] arr, int x) {
        int low = 0, high = arr.length - 1;
        int count = 0;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (arr[mid] < x) {
                count = mid + 1;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return count;
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