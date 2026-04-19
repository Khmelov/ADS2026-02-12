package by.it.group510901.poltorak.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


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
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }
        quickSortStops(stops, 0, n - 1);
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int firstInvalid = findFirstGreater(starts, point);
            int firstValidStop = findFirstGreaterOrEqual(stops, point);
            result[i] = firstInvalid - firstValidStop;
        }

        return result;
    }
    private void quickSort3Way(Segment[] arr, int left, int right) {
        if (left >= right) return;
        int pivotIndex = medianOfThree(arr, left, right);
        Segment pivot = arr[pivotIndex];
        int lt = left;
        int i = left;
        int gt = right;

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
        if (lt - left < right - gt) {
            quickSort3Way(arr, left, lt - 1);
            quickSort3Way(arr, gt + 1, right);
        } else {
            quickSort3Way(arr, gt + 1, right);
            quickSort3Way(arr, left, lt - 1);
        }
    }
    private int medianOfThree(Segment[] arr, int left, int right) {
        int mid = left + (right - left) / 2;
        if (arr[mid].compareTo(arr[left]) < 0) swap(arr, left, mid);
        if (arr[right].compareTo(arr[left]) < 0) swap(arr, left, right);
        if (arr[right].compareTo(arr[mid]) < 0) swap(arr, mid, right);
        return mid;
    }
    private void quickSortStops(int[] arr, int left, int right) {
        if (left >= right) return;

        int pivot = arr[left + (right - left) / 2];
        int lt = left;
        int i = left;
        int gt = right;

        while (i <= gt) {
            if (arr[i] < pivot) {
                swap(arr, lt, i);
                lt++;
                i++;
            } else if (arr[i] > pivot) {
                swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }

        quickSortStops(arr, left, lt - 1);
        quickSortStops(arr, gt + 1, right);
    }
    private int findFirstGreater(int[] arr, int x) {
        int left = 0;
        int right = arr.length - 1;
        int result = arr.length;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] > x) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }
    private int findFirstGreaterOrEqual(int[] arr, int x) {
        int left = 0;
        int right = arr.length - 1;
        int result = arr.length;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] >= x) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
            return Integer.compare(this.start, o.start);
        }
    }

}
