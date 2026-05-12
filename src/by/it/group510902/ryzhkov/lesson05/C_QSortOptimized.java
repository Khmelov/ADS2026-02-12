package by.it.group510902.ryzhkov.lesson05;

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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        // !!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        // число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        // число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // читаем сами отрезки
        for (int i = 0; i < n; i++) {
            // читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        // тут реализуйте логику задачи с применением быстрой сортировки
        // в классе отрезка Segment реализуйте нужный для этой задачи компаратор
         quickSortSegments(segments, 0, n - 1);

        int[] starts = new int[n];
        int[] stops  = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i]  = segments[i].stop;
        }

        quickSortInt(stops, 0, n - 1);

        for (int j = 0; j < m; j++) {
            int x = points[j];
            int countStarts = countLessOrEqual(starts, x);
            int countEndsLess = countStrictlyLess(stops, x);
            result[j] = countStarts - countEndsLess;
        }
        
        return result;
    }

    private void quickSortSegments(Segment[] arr, int lo, int hi) {
        while (lo < hi) {

            Segment pivot = arr[lo];
            int lt = lo, gt = hi;
            int i = lo + 1;

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

            if (lt - lo < hi - gt) {
                quickSortSegments(arr, lo, lt - 1);  
                lo = gt + 1;                       
            } else {
                quickSortSegments(arr, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private void quickSortInt(int[] arr, int lo, int hi) {
        while (lo < hi) {
            int pivot = arr[lo];
            int lt = lo, gt = hi;
            int i = lo + 1;
            while (i <= gt) {
                if (arr[i] < pivot) {
                    swapInt(arr, lt++, i++);
                } else if (arr[i] > pivot) {
                    swapInt(arr, i, gt--);
                } else {
                    i++;
                }
            }
            if (lt - lo < hi - gt) {
                quickSortInt(arr, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSortInt(arr, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private void swapInt(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    private int countLessOrEqual(int[] arr, int x) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] <= x) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo; 
    }

    private int countStrictlyLess(int[] arr, int x) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] < x) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            // подумайте, что должен возвращать компаратор отрезков
            return 0;
        }
    }

}
