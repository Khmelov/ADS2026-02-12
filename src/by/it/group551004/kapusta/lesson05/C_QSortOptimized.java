package by.it.group551004.kapusta.lesson05;

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

        // число отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];

        // число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // читаем отрезки
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // сортировка отрезков (in-place, 3-way quicksort)
        quickSort3(segments, 0, n - 1);

        // для каждой точки — бинарный поиск + подсчёт
        for (int i = 0; i < m; i++) {
            result[i] = countSegments(segments, points[i]);
        }

        return result;
    }

    // ------------------ 3‑разбиение + устранение хвостовой рекурсии ------------------

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

    // ------------------ Поиск количества подходящих отрезков ------------------

    private int countSegments(Segment[] segments, int point) {
        int n = segments.length;

        // бинарный поиск последнего сегмента, у которого start <= point
        int left = 0, right = n - 1, pos = -1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (segments[mid].start <= point) {
                pos = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        if (pos == -1) return 0;

        // идём назад и считаем подходящие сегменты
        int count = 0;
        for (int i = pos; i >= 0; i--) {
            if (segments[i].start <= point && point <= segments[i].stop) {
                count++;
            } else if (segments[i].start > point) {
                break;
            }
        }

        return count;
    }

    // ------------------ Класс отрезка ------------------

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
            return Integer.compare(this.start, o.start);
        }
    }
}
