package by.it.group551003.kalach.lesson05;

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

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по началу
        qSort(segments, 0, n - 1);

        // Извлекаем начала и концы
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        // Сортируем концы отдельно
        qSortInt(stops, 0, n - 1);

        // Для каждой точки считаем количество отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Отрезков начавшихся <= point минус отрезков закончившихся < point
            result[i] = upperBound(starts, point) - lowerBound(stops, point);
        }

        return result;
    }

    private void qSort(Segment[] arr, int lo, int hi) {
        if (lo >= hi) return;
        Segment pivot = arr[(lo + hi) / 2];
        int i = lo, j = hi;
        while (i <= j) {
            while (arr[i].compareTo(pivot) < 0) i++;
            while (arr[j].compareTo(pivot) > 0) j--;
            if (i <= j) {
                Segment tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        qSort(arr, lo, j);
        qSort(arr, i, hi);
    }

    private void qSortInt(int[] arr, int lo, int hi) {
        if (lo >= hi) return;
        int pivot = arr[(lo + hi) / 2];
        int i = lo, j = hi;
        while (i <= j) {
            while (arr[i] < pivot) i++;
            while (arr[j] > pivot) j--;
            if (i <= j) {
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        qSortInt(arr, lo, j);
        qSortInt(arr, i, hi);
    }

    // Количество элементов arr[i] <= target
    private int upperBound(int[] arr, int target) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (arr[mid] <= target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    // Количество элементов arr[i] < target
    private int lowerBound(int[] arr, int target) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (arr[mid] < target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) return Integer.compare(this.start, o.start);
            return Integer.compare(this.stop, o.stop);
        }
    }

}