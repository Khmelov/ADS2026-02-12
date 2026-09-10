package by.it.group510902.paleychik.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
        InputStream stream = by.it.group510902.paleychik.lesson05.C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        by.it.group510902.paleychik.lesson05.C_QSortOptimized instance = new by.it.group510902.paleychik.lesson05.C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        by.it.group510902.paleychik.lesson05.C_QSortOptimized.Segment[] segments = new by.it.group510902.paleychik.lesson05.C_QSortOptimized.Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new by.it.group510902.paleychik.lesson05.C_QSortOptimized.Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++){
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        quickSort(starts, 0, n - 1);
        quickSort(ends, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int gotStarted = 0;
            int l = 0;
            int r = n - 1;
            while (l <= r) {
                int mid = (l + r) / 2;
                if (starts[mid] <= p) {
                    gotStarted = mid + 1;
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }

            int gotEnded = 0;
            l = 0;
            r = n - 1;
            while (l <= r) {
                int mid = (l + r) / 2;
                if (ends[mid] < p) {
                    gotEnded = mid + 1;
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
            result[i] = gotStarted - gotEnded;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    private void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    private int[] partition(int[] a, int l, int r) {
        int pivot = a[l + (r - l) / 2];

        int lt = l;
        int i = l;
        int gt = r;

        while (i <= gt) {
            if (a[i] < pivot) {
                swap(a, lt++, i++);
            } else if (a[i] > pivot) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }

        return new int[]{lt, gt};
    }
    private void quickSort(int[] a, int l, int r) {
        while (l < r) {
            int[] p = partition(a, l, r);
            int lt = p[0];
            int gt = p[1];

            // рекурсия только на меньшую часть (оптимизация стека)
            if (lt - l < r - gt) {
                quickSort(a, l, lt - 1);
                l = gt + 1; // хвостовая рекурсия
            } else {
                quickSort(a, gt + 1, r);
                r = lt - 1;
            }
        }
    }
    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            return 0;
        }
    }

}