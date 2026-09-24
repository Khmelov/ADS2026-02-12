package by.it.group551003.ogonovsky.lesson05;

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

        // сортировка отрезков на месте по полю start (компаратор внутри Segment)
        quickSort3(segments, 0, n - 1);

        // отдельные массивы начал и концов для бинарного поиска
        // starts уже отсортирован (как побочный эффект сортировки segments по start)
        // stops нужно отсортировать отдельно
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }
        quickSortInts3(stops, 0, n - 1);

        // для каждой точки x:
        //   находим первый индекс в starts, где start > x  -> кол-во начал <= x
        //   находим первый индекс в stops,  где stop  >= x -> кол-во концов <  x
        //   ответ = (начал <= x) - (концов < x)
        for (int i = 0; i < m; i++) {
            int x = points[i];
            int startsLE = upperBound(starts, x);
            int stopsLT  = lowerBound(stops, x);
            result[i] = startsLE - stopsLT;
        }

        return result;
    }

    // ---- QuickSort с 3-разбиением и элиминацией хвостовой рекурсии (для Segment[]) ----
    private void quickSort3(Segment[] a, int lo, int hi) {
        while (lo < hi) {
            // рандомизация pivot для защиты от худшего случая
            int pivotIdx = lo + (int) (Math.random() * (hi - lo + 1));
            int pivot = a[pivotIdx].start;

            // 3-разбиение по ключу start: < pivot | == pivot | > pivot
            int lt = lo, gt = hi, i = lo;
            while (i <= gt) {
                int cmp = Integer.compare(a[i].start, pivot);
                if (cmp < 0) {
                    swap(a, lt++, i++);
                } else if (cmp > 0) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }
            // [lo..lt-1] < pivot, [lt..gt] == pivot, [gt+1..hi] > pivot

            // рекурсия в меньшую часть, итерация по большей (хвостовая рекурсия -> цикл)
            if (lt - lo < hi - gt) {
                quickSort3(a, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSort3(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment t = a[i]; a[i] = a[j]; a[j] = t;
    }

    // ---- та же 3-way QuickSort с TCE для int[] (для массива stops) ----
    private void quickSortInts3(int[] a, int lo, int hi) {
        while (lo < hi) {
            int pivotIdx = lo + (int) (Math.random() * (hi - lo + 1));
            int pivot = a[pivotIdx];

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

            if (lt - lo < hi - gt) {
                quickSortInts3(a, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSortInts3(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    // ---- бинарный поиск ----
    // первый индекс, где a[idx] >= x   (= количество элементов < x)
    private int lowerBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < x) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    // первый индекс, где a[idx] > x    (= количество элементов <= x)
    private int upperBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] <= x) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            // нормализуем порядок концов на всякий случай
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
            // компаратор для сортировки отрезков по началу
            Segment other = (Segment) o;
            return Integer.compare(this.start, other.start);
        }
    }
}