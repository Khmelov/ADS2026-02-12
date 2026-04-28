package by.it.group551001.sidorova.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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

        // сортировка отрезков по start (in-place, 3-way partition)
        quickSort3(segments, 0, n - 1);

        // обработка точек
        for (int i = 0; i < m; i++) {
            int p = points[i];
            result[i] = countSegments(segments, p);
        }

        return result;
    }

    // ---------------------------------------------------------
    // 3‑way quicksort + tail recursion elimination
    // ---------------------------------------------------------
    private void quickSort3(Segment[] a, int left, int right) {
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

            // рекурсивно сортируем меньшую часть
            if (lt - left < right - gt) {
                quickSort3(a, left, lt - 1);
                left = gt + 1; // хвостовая рекурсия
            } else {
                quickSort3(a, gt + 1, right);
                right = lt - 1; // хвостовая рекурсия
            }
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // ---------------------------------------------------------
    // Поиск количества отрезков, содержащих точку p
    // ---------------------------------------------------------
    private int countSegments(Segment[] a, int p) {
        int idx = binarySearchFirst(a, p);
        if (idx == -1) return 0;

        int count = 0;

        // идём влево
        int i = idx;
        while (i >= 0 && a[i].contains(p)) {
            count++;
            i--;
        }

        // идём вправо
        i = idx + 1;
        while (i < a.length && a[i].contains(p)) {
            count++;
            i++;
        }

        return count;
    }

    // бинарный поиск первого подходящего отрезка
    private int binarySearchFirst(Segment[] a, int p) {
        int left = 0, right = a.length - 1;
        int res = -1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (a[mid].contains(p)) {
                res = mid;
                right = mid - 1; // ищем первый
            } else if (a[mid].start > p) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return res;
    }

    // ---------------------------------------------------------
    // Класс Segment
    // ---------------------------------------------------------
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

        boolean contains(int p) {
            return p >= start && p <= stop;
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}
