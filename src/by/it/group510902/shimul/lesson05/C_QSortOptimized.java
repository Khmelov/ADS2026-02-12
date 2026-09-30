package by.it.group510902.shimul.lesson05;

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


import java.io.*;
import java.util.*;

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
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // 1. Быстрая сортировка 3-way + tail recursion elimination
        quickSort3(segments, 0, n - 1);

        // 2. Для каждой точки ищем первый подходящий отрезок бинарным поиском
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int idx = firstSegment(segments, p);

            int count = 0;
            while (idx < n && segments[idx].start <= p) {
                if (segments[idx].stop >= p) count++;
                idx++;
            }
            result[i] = count;
        }

        return result;
    }

    // ---------- Бинарный поиск первого отрезка start <= point ----------
    private int firstSegment(Segment[] a, int point) {
        int l = 0, r = a.length - 1;
        int ans = a.length;

        while (l <= r) {
            int mid = (l + r) / 2;
            if (a[mid].start <= point) {
                ans = mid;
                r = mid - 1;
            } else l = mid + 1;
        }
        return ans;
    }

    // ---------- QuickSort 3-way ----------
    private void quickSort3(Segment[] a, int l, int r) {
        while (l < r) {
            Segment pivot = a[l + (r - l) / 2];

            int lt = l, i = l, gt = r;

            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) swap(a, lt++, i++);
                else if (cmp > 0) swap(a, i, gt--);
                else i++;
            }

            // рекурсия только в меньшую часть (tail elimination)
            if (lt - l < r - gt) {
                quickSort3(a, l, lt - 1);
                l = gt + 1;
            } else {
                quickSort3(a, gt + 1, r);
                r = lt - 1;
            }
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // ---------- Segment ----------
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Object o) {
            Segment s = (Segment) o;
            return Integer.compare(this.start, s.start);
        }
    }
}
