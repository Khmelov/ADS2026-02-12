package by.it.group551004.kunikin.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];
        int[] starts = new int[n];
        int[] stops = new int[n];

        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            segments[i] = new Segment(a, b);
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        quickSort(starts, 0, n - 1);
        quickSort(stops, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int started = countLessOrEqual(starts, p);
            if (started == 0) {
                result[i] = 0;
                continue;
            }
            int ended = countLess(stops, p);
            result[i] = started - ended;
        }
        return result;
    }

    private void quickSort(int[] a, int left, int right) {
        if (left >= right) return;
        int i = left, j = right;
        int pivot = a[left + (right - left) / 2];

        while (i <= j) {
            while (a[i] < pivot) i++;
            while (a[j] > pivot) j--;
            if (i <= j) {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                i++;
                j--;
            }
        }
        quickSort(a, left, j);
        quickSort(a, i, right);
    }

    private int countLessOrEqual(int[] arr, int x) {
        int l = 0, r = arr.length - 1;
        int count = 0;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (arr[mid] <= x) {
                count = mid + 1;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return count;
    }

    private int countLess(int[] arr, int x) {
        int l = 0, r = arr.length - 1;
        int count = 0;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (arr[mid] < x) {
                count = mid + 1;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return count;
    }

    private class Segment implements Comparable<Segment> {
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
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

}
