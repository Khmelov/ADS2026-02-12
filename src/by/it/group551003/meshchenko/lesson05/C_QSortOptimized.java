package by.it.group551003.meshchenko.lesson05;

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
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // optimized quick sort on segments (3-way partition, tail recursion elimination)
        quickSortOptimized(segments, 0, n - 1);

        // extract and sort stops using the same optimized quick sort
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = segments[i].stop;
        }
        quickSortOptimized(stops, 0, n - 1);

        // answer queries with binary search
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int cntStart = countLessOrEqual(segments, point);
            int cntStop = countLess(stops, point);
            result[i] = cntStart - cntStop;
        }
        return result;
    }

    // ---------- Optimised QuickSort with 3-way partitioning and tail recursion elimination ----------
    private void quickSortOptimized(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] p = partition3way(arr, low, high); // returns [lt, gt]
            int lt = p[0], gt = p[1];
            // recurse on smaller part, iterate on larger part
            if (lt - low < high - gt) {
                quickSortOptimized(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSortOptimized(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    // 3-way partition for Segment[] using start field
    private int[] partition3way(Segment[] arr, int low, int high) {
        int pivot = arr[low].start;  // choose first element as pivot
        int lt = low, i = low, gt = high;
        while (i <= gt) {
            int cmp = Integer.compare(arr[i].start, pivot);
            if (cmp < 0) {
                swap(arr, lt++, i++);
            } else if (cmp > 0) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    // Overloaded for int[] array (stops)
    private void quickSortOptimized(int[] arr, int low, int high) {
        while (low < high) {
            int[] p = partition3wayInt(arr, low, high);
            int lt = p[0], gt = p[1];
            if (lt - low < high - gt) {
                quickSortOptimized(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSortOptimized(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private int[] partition3wayInt(int[] arr, int low, int high) {
        int pivot = arr[low];
        int lt = low, i = low, gt = high;
        while (i <= gt) {
            int cmp = Integer.compare(arr[i], pivot);
            if (cmp < 0) {
                int tmp = arr[lt];
                arr[lt] = arr[i];
                arr[i] = tmp;
                lt++; i++;
            } else if (cmp > 0) {
                int tmp = arr[i];
                arr[i] = arr[gt];
                arr[gt] = tmp;
                gt--;
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    // ---------- Binary search helpers ----------
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
        return low;
    }

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
        return low;
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
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) return Integer.compare(this.start, o.start);
            return Integer.compare(this.stop, o.stop);
        }
    }
}