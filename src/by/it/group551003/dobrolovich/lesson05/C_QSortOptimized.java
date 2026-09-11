package by.it.group551003.dobrolovich.lesson05;

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
        int m = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        quickSort3Way(segments, 0, n - 1);
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int firstIndex = findFirstSegment(segments, point);
            int count = 0;
            for (int j = firstIndex; j >= 0 && segments[j].start <= point; j--) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    private int[] threeWayPartition(Segment[] arr, int low, int high) {
        Segment pivot = arr[low];
        int lt = low;
        int i = low + 1;
        int gt = high;
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

    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] pivots = threeWayPartition(arr, low, high);
            int lt = pivots[0];
            int gt = pivots[1];
            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;  // хвостовая рекурсия для большей части
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private int findFirstSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                left = mid + 1;  // ищем правее
            } else {
                right = mid - 1;
            }
        }

        return result;
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
            // Гарантируем, что start <= stop
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment other) {
            // Сравниваем по началу отрезка
            return Integer.compare(this.start, other.start);
        }

        @Override
        public String toString() {
            return "[" + start + ", " + stop + "]";
        }
    }
}