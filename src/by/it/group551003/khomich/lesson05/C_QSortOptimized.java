package by.it.group551003.khomich.lesson05;

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
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        quickSort3Way(segments, 0, n - 1);

        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        quickSortInt(stops, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int cntStart = upperBound(starts, point);
            int cntStop = lowerBound(stops, point);
            result[i] = cntStart - cntStop;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort3Way(Segment[] a, int l, int r) {
        while (l < r) {
            int lt = l, i = l, gt = r;
            Segment pivot = a[l];
            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(a, lt, i);
                    lt++;
                    i++;
                } else if (cmp > 0) {
                    swap(a, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }
            if (lt - l < r - gt) {
                quickSort3Way(a, l, lt - 1);
                l = gt + 1;
            } else {
                quickSort3Way(a, gt + 1, r);
                r = lt - 1;
            }
        }
    }

    private void quickSortInt(int[] a, int l, int r) {
        while (l < r) {
            int lt = l, i = l, gt = r;
            int pivot = a[l];
            while (i <= gt) {
                if (a[i] < pivot) {
                    swap(a, lt, i);
                    lt++;
                    i++;
                } else if (a[i] > pivot) {
                    swap(a, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }
            if (lt - l < r - gt) {
                quickSortInt(a, l, lt - 1);
                l = gt + 1;
            } else {
                quickSortInt(a, gt + 1, r);
                r = lt - 1;
            }
        }
    }
    private void swap(Segment[] a, int i, int j) {
        Segment tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    private void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    private int upperBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (a[mid] <= x) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
    private int lowerBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (a[mid] < x) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            }else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            if (this.start != o.start)
                return Integer.compare(this.start, o.start);
            else
                return Integer.compare(this.stop, o.stop);

        }
    }

}
