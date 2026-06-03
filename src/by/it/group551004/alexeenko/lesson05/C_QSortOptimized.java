package by.it.group551004.alexeenko.lesson05;

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
        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // бинарный поиск первого отрезка у которого stop >= point
            int lo = 0, hi = n - 1, first = n;
            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;
                if (segments[mid].stop >= point) {
                    first = mid;
                    hi = mid - 1;
                } else {
                    lo = mid + 1;
                }
            }
            // линейно проходим оставшиеся отрезки начиная с first
            for (int j = first; j < n; j++) {
                if (segments[j].start > point) break;
                if (segments[j].start <= point && point <= segments[j].stop)
                    count++;
            }
            result[i] = count;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort(Segment[] a, int lo, int hi) {
        while (lo < hi) {
            // 3-разбиение (Дейкстра)
            int lt = lo, gt = hi, i = lo + 1;
            Segment pivot = a[lo];
            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if      (cmp < 0) swap(a, lt++, i++);
                else if (cmp > 0) swap(a, i, gt--);
                else              i++;
            }
            // рекурсия на меньшей части, итерация на большей (элиминация хвостовой рекурсии)
            if (lt - lo < hi - gt) {
                quickSort(a, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSort(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment tmp = a[i]; a[i] = a[j]; a[j] = tmp;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            Segment other = (Segment) o;
            if (this.stop != other.stop)
                return Integer.compare(this.stop, other.stop);
            return Integer.compare(this.start, other.start);
        }
    }

}