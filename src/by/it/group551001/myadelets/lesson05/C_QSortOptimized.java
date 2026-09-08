package by.it.group551001.myadelets.lesson05;

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
            segments[i] = new Segment(a, b);
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        quickSortSegments(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            result[i] = countSegmentsForPoint(segments, p);
        }

        return result;
    }

    private int countSegmentsForPoint(Segment[] segs, int p) {
        int n = segs.length;

        int idx = firstSegmentWithStopAtLeast(segs, p);
        if (idx == -1) {
            return 0;
        }

        int count = 0;

        int i = idx;
        while (i >= 0 && segs[i].stop >= p) {
            if (segs[i].start <= p && p <= segs[i].stop) {
                count++;
            }
            i--;
        }

        i = idx + 1;
        while (i < n && segs[i].start <= p && segs[i].stop >= p) {
            count++;
            i++;
        }

        return count;
    }

    private int firstSegmentWithStopAtLeast(Segment[] segs, int p) {
        int left = 0;
        int right = segs.length - 1;
        int ans = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segs[mid].stop >= p) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    private void quickSortSegments(Segment[] a, int left, int right) {
        while (left < right) {
            // 3-разбиение: < pivot, == pivot, > pivot
            int lt = left;
            int gt = right;
            Segment pivot = a[left + (right - left) / 2];
            int i = left;

            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(a, lt, i);
                    lt++;
                    i++;
                } else if (cmp > 0) {
                    swap(a, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }

            if (lt - left < right - gt) {
                if (left < lt - 1) {
                    quickSortSegments(a, left, lt - 1);
                }
                left = gt + 1;
            } else {
                if (gt + 1 < right) {
                    quickSortSegments(a, gt + 1, right);
                }
                right = lt - 1;
            }
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
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
