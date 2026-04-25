package by.it.group551003.kalach.lesson05;

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

        // 1. Быстрая сортировка на месте с 3-разбиением и элиминацией рекурсии
        qSort(segments, 0, n - 1);

        // 2. Поиск количества покрывающих отрезков для каждой точки
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Используем бинарный поиск для поиска границы, за которой start > point
            // Нам нужны все отрезки, где segments[j].start <= point
            int rightBound = findRightBound(segments, point);

            int count = 0;
            for (int j = 0; j <= rightBound; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    private void qSort(Segment[] a, int left, int right) {
        while (left < right) {
            // 3-разбиение
            int[] m = partition(a, left, right);

            // Элиминация хвостовой рекурсии: выбираем меньшую часть для рекурсии
            if (m[0] - left < right - m[1]) {
                qSort(a, left, m[0] - 1);
                left = m[1] + 1;
            } else {
                qSort(a, m[1] + 1, right);
                right = m[0] - 1;
            }
        }
    }

    private int[] partition(Segment[] a, int left, int right) {
        Segment pivot = a[left + (int)(Math.random() * (right - left + 1))];
        int lt = left;
        int i = left;
        int gt = right;

        while (i <= gt) {
            int cmp = a[i].compareTo(pivot);
            if (cmp < 0) {
                swap(a, lt++, i++);
            } else if (cmp > 0) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    private void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private int findRightBound(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int ans = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                ans = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
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