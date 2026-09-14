package by.it.group551003.ogonovsky.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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

        // выделяем начала и концы в отдельные массивы
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        // сортируем быстрой сортировкой
        quickSort(starts, 0, n - 1);
        quickSort(stops, 0, n - 1);

        // для каждой точки x:
        //   countStartsLE(x) - количество начал <= x
        //   countStopsLT(x)  - количество концов < x
        //   ответ = countStartsLE(x) - countStopsLT(x)
        for (int i = 0; i < m; i++) {
            int x = points[i];
            int startsLE = upperBound(starts, x);   // число элементов <= x
            int stopsLT  = lowerBound(stops, x);    // число элементов < x
            result[i] = startsLE - stopsLT;
        }

        return result;
    }

    // быстрая сортировка с рандомизированным опорным элементом
    private void quickSort(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int pivotIndex = lo + (int) (Math.random() * (hi - lo + 1));
        int pivot = a[pivotIndex];
        // трёхпутевое разбиение (защита от множества равных элементов)
        int lt = lo, gt = hi, i = lo;
        while (i <= gt) {
            if (a[i] < pivot) {
                int t = a[lt]; a[lt] = a[i]; a[i] = t;
                lt++; i++;
            } else if (a[i] > pivot) {
                int t = a[i]; a[i] = a[gt]; a[gt] = t;
                gt--;
            } else {
                i++;
            }
        }
        quickSort(a, lo, lt - 1);
        quickSort(a, gt + 1, hi);
    }

    // число элементов в отсортированном массиве, строго меньших x
    private int lowerBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < x) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    // число элементов в отсортированном массиве, меньших или равных x
    private int upperBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] <= x) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // если концы пришли в обратном порядке - меняем местами
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
            // сортировка отрезков по началу, при равных - по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}