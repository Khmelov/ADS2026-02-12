package by.it.group551004.karpovich.lesson05;

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
        quickSort3(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int idx = binarySearchFirst(segments, p);

            if (idx == -1) {
                result[i] = 0;
                continue;
            }

            int count = 0;

            int j = idx;
            while (j < n && segments[j].start <= p && segments[j].stop >= p) {
                count++;
                j++;
            }

            j = idx - 1;
            while (j >= 0 && segments[j].start <= p && segments[j].stop >= p) {
                count++;
                j--;
            }

            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private static void quickSort3(Segment[] a, int left, int right) {
        while (left < right) {
            int lt = left, gt = right;
            Segment pivot = a[left];
            int i = left;

            while (i <= gt) {
                if (a[i].compareTo(pivot) < 0) {
                    swap(a, lt++, i++);
                } else if (a[i].compareTo(pivot) > 0) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }

            if (lt - left < right - gt) {
                quickSort3(a, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort3(a, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private static void swap(Segment[] a, int i, int j) {
        Segment t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static int binarySearchFirst(Segment[] a, int p) {
        int left = 0, right = a.length - 1;
        int res = -1;

        while (left <= right) {
            int mid = (left + right) / 2;
            Segment s = a[mid];

            if (s.start <= p && s.stop >= p) {
                res = mid;
                right = mid - 1;
            } else if (s.start > p) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return res;
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
            Segment s = (Segment) o;
            //подумайте, что должен возвращать компаратор отрезков
            if (this.start != s.start)
                return Integer.compare(this.start, s.start);
            return Integer.compare(this.stop, s.stop);
        }
    }

}
