package by.it.group551001.zlotnikov.lesson05;

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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
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
        quickSortSegments3(segments, 0, n - 1);

        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = segments[i].stop;
        }
        quickSortInt3(stops, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int x = points[i];
            // Количество начал ≤ x
            int cntStart = upperBoundStart(segments, x);
            // Количество концов < x
            int cntStop = lowerBoundStop(stops, x);
            result[i] = cntStart - cntStop;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            return Integer.compare(this.start, other.start);
        }
    }

    // ========== Сортировка отрезков (по start) ==========
    private void quickSortSegments3(Segment[] arr, int lo, int hi) {
        while (lo < hi) {
            int[] piv = partition3Segments(arr, lo, hi);
            int lt = piv[0], gt = piv[1];
            if (lt - lo < hi - gt) {
                quickSortSegments3(arr, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSortSegments3(arr, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private int[] partition3Segments(Segment[] arr, int lo, int hi) {
        int mid = lo + (hi - lo) / 2;
        Segment pivot = arr[mid];
        int lt = lo, i = lo, gt = hi;
        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0)       swap(arr, lt++, i++);
            else if (cmp > 0)  swap(arr, i, gt--);
            else               i++;
        }
        return new int[]{lt, gt};
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private void quickSortInt3(int[] arr, int lo, int hi) {
        while (lo < hi) {
            int[] piv = partition3Int(arr, lo, hi);
            int lt = piv[0], gt = piv[1];
            if (lt - lo < hi - gt) {
                quickSortInt3(arr, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSortInt3(arr, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private int[] partition3Int(int[] arr, int lo, int hi) {
        int mid = lo + (hi - lo) / 2;
        int pivot = arr[mid];
        int lt = lo, i = lo, gt = hi;
        while (i <= gt) {
            if (arr[i] < pivot)      { int t = arr[lt]; arr[lt] = arr[i]; arr[i] = t; lt++; i++; }
            else if (arr[i] > pivot) { int t = arr[i]; arr[i] = arr[gt]; arr[gt] = t; gt--; }
            else                     i++;
        }
        return new int[]{lt, gt};
    }

    private int upperBoundStart(Segment[] seg, int x) {
        int left = 0, right = seg.length - 1, ans = 0;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            if (seg[mid].start <= x) {
                ans = mid + 1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
    }

    private int lowerBoundStop(int[] stops, int x) {
        int left = 0, right = stops.length - 1, ans = 0;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            if (stops[mid] < x) {
                ans = mid + 1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
    }

}
