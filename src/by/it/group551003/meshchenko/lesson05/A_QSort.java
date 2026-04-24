package by.it.group551003.meshchenko.lesson05;

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

        // sort segments by start using quick sort
        quickSort(segments, 0, n - 1);

        // extract and sort stops
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = segments[i].stop;
        }
        quickSort(stops, 0, n - 1);

        // for each point compute answer
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int countStart = countLessOrEqual(segments, point);
            int countStop = countLess(stops, point);
            result[i] = countStart - countStop;
        }
        return result;
    }

    // ----- Quick sort for Segment[] (by start) -----
    private void quickSort(Segment[] arr, int low, int high) {
        if (low >= high) return;
        int i = low, j = high;
        int pivot = arr[low + (high - low) / 2].start;
        while (i <= j) {
            while (arr[i].start < pivot) i++;
            while (arr[j].start > pivot) j--;
            if (i <= j) {
                swap(arr, i, j);
                i++;
                j--;
            }
        }
        if (low < j) quickSort(arr, low, j);
        if (i < high) quickSort(arr, i, high);
    }

    // ----- Quick sort for int[] -----
    private void quickSort(int[] arr, int low, int high) {
        if (low >= high) return;
        int i = low, j = high;
        int pivot = arr[low + (high - low) / 2];
        while (i <= j) {
            while (arr[i] < pivot) i++;
            while (arr[j] > pivot) j--;
            if (i <= j) {
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        if (low < j) quickSort(arr, low, j);
        if (i < high) quickSort(arr, i, high);
    }

    // count elements with start <= key
    private int countLessOrEqual(Segment[] arr, int key) {
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (arr[mid].start <= key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low; // number of elements <= key
    }

    // count elements with value < key
    private int countLess(int[] arr, int key) {
        int low = 0, high = arr.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (arr[mid] < key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low; // number of elements < key
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start > stop) { // ensure correct order
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            // compare by start, then by stop for stable ordering
            if (this.start != o.start) return Integer.compare(this.start, o.start);
            return Integer.compare(this.stop, o.stop);
        }
    }
}