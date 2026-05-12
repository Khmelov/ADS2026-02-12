package by.it.group551002.brutski.lesson05;

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
        int[] starts = new int[n];
        int[] stops = new int[n];

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

        QuickSortOptimized(segments, 0, n - 1);
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int starts_less = bin_search_count(starts, point + 1);
            int stops_less = bin_search_count(stops, point);
            result[i] = starts_less - stops_less;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
            Segment comp = (Segment) o;

            if (this.start != comp.start)
                return Integer.compare(this.start, comp.start);

            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.stop, comp.stop);
        }
    }

    Segment GetMid(Segment a, Segment b, Segment c) {
        if (a.compareTo(b) >= 0 && b.compareTo(c) >= 0 || c.compareTo(b) >= 0 && b.compareTo(a) >= 0)
            return b;

        if (b.compareTo(a) >= 0 && a.compareTo(c) >= 0 || c.compareTo(a) >= 0 && a.compareTo(b) >= 0)
            return a;

        return c;
    }

    void QuickSortOptimized(Segment[] segs, int l, int r) {
        while (l < r) {
            int less = l;
            int i = less + 1, gr = r;
            Segment pivot = GetMid(segs[l], segs[(l + r) / 2], segs[r]);

            while (i <= gr) {
                if (segs[i].compareTo(pivot) < 0) {
                    Segment temp = segs[less];
                    segs[less] = segs[i];
                    segs[i] = temp;
                    less++;
                    i++;
                }
                else if (segs[i].compareTo(pivot) > 0) {
                    Segment temp = segs[gr];
                    segs[gr] = segs[i];
                    segs[i] = temp;
                    gr--;
                }
                else
                    i++;
            }

            if (less - l < r - gr) {
                QuickSortOptimized(segs, l, less - 1);
                l = gr + 1;
            }
            else {
                QuickSortOptimized(segs, gr + 1, r);
                r = less - 1;
            }
        }
    }

    int bin_search_count(int[] a, int val) {
        int l = 0, r = a.length - 1;
        int count = 0;

        while (l <= r) {
            int mid = (l + r) / 2;
            if (a[mid] < val) {
                l = mid + 1;
                count = mid + 1;
            }
            else
                r = mid - 1;
        }

        return count;
    }
}