package by.it.group551001.Barusevich.lesson01.lesson05;

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

        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];

            int left = 0;
            int right = n - 1;
            int first = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    first = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            int count = 0;
            for (int j = 0; j <= first; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    private void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] partitions = partition(arr, low, high);
            int lt = partitions[0];
            int gt = partitions[1];

            if (lt - low < high - gt) {
                quickSort(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private int[] partition(Segment[] arr, int low, int high) {
        int lt = low;
        int i = low + 1;
        int gt = high;
        Segment pivot = arr[low];

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, lt, i);
                lt++;
                i++;
            } else if (cmp > 0) {
                swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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