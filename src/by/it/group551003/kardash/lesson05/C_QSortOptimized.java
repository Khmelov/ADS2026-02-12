package by.it.group551003.kardash.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;
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

        quickSort3Way(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int leftCount = countLeft(segments, n, point);
            int rightCount = countRight(segments, n, point);
            result[i] = leftCount - rightCount;
        }

        return result;
    }

    void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] pivots = partition3Way(arr, low, high);
            int lt = pivots[0];
            int gt = pivots[1];

            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    int[] partition3Way(Segment[] arr, int low, int high) {
        int pivotIdx = low + (int)(Math.random() * (high - low + 1));
        Segment pivot = arr[pivotIdx];
        swap(arr, low, pivotIdx);

        int lt = low;
        int gt = high;
        int i = low + 1;

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
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

    void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    int countLeft(Segment[] segments, int n, int point) {
        int left = 0, right = n;
        while (left < right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    int countRight(Segment[] segments, int n, int point) {
        int left = 0, right = n;
        while (left < right) {
            int mid = (left + right) / 2;
            if (segments[mid].stop < point) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
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