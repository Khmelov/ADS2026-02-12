package by.it.group510901.tsimoshenko.lesson05;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Оптимизированное решение с:
- 3-разбиением в быстрой сортировке
- элиминацией хвостовой рекурсии
- сортировкой на месте
- бинарным поиском для подсчёта покрытия
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
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Начало задачи
        quickSort3Way(segments, 0, n - 1, true);

        Segment[] byEnd = segments.clone();
        quickSort3Way(byEnd, 0, n - 1, false);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int started = upperBoundStart(segments, p);   // start <= p
            int ended = lowerBoundEnd(byEnd, p);           // stop < p
            result[i] = started - ended;
        }

        return result;
    }

    private void quickSort3Way(Segment[] arr, int lo, int hi, boolean sortByStart) {
        while (lo < hi) {
            if (hi - lo < 10) {
                insertionSort(arr, lo, hi, sortByStart);
                break;
            }

            int mid = lo + (hi - lo) / 2;
            medianOfThree(arr, lo, mid, hi, sortByStart);
            swap(arr, lo, mid);
            int pivot = sortByStart ? arr[lo].start : arr[lo].stop;

            int lt = lo, gt = hi, i = lo + 1;
            while (i <= gt) {
                int cmp = compareValue(arr[i], pivot, sortByStart);
                if (cmp < 0) {
                    swap(arr, lt++, i++);
                } else if (cmp > 0) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }

            if (lt - lo < hi - gt) {
                quickSort3Way(arr, lo, lt - 1, sortByStart);
                lo = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, hi, sortByStart);
                hi = lt - 1;
            }
        }
    }

    private void insertionSort(Segment[] arr, int lo, int hi, boolean sortByStart) {
        for (int i = lo + 1; i <= hi; i++) {
            Segment key = arr[i];
            int j = i - 1;
            while (j >= lo && compare(arr[j], key, sortByStart) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private void medianOfThree(Segment[] arr, int a, int b, int c, boolean sortByStart) {
        if (compare(arr[a], arr[b], sortByStart) > 0) swap(arr, a, b);
        if (compare(arr[a], arr[c], sortByStart) > 0) swap(arr, a, c);
        if (compare(arr[b], arr[c], sortByStart) > 0) swap(arr, b, c);
    }

    private int compare(Segment x, Segment y, boolean sortByStart) {
        return sortByStart ? Integer.compare(x.start, y.start)
                : Integer.compare(x.stop, y.stop);
    }

    private int compareValue(Segment x, int pivot, boolean sortByStart) {
        return sortByStart ? Integer.compare(x.start, pivot)
                : Integer.compare(x.stop, pivot);
    }

    private void swap(Segment[] arr, int i, int j) {
        if (i == j) return;
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    
    private int upperBoundStart(Segment[] arr, int key) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1; // безопасное деление на 2
            if (arr[mid].start <= key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo; // ровно 'lo' элементов имеют start <= key
    }

    /**
     * Нижняя граница: количество отрезков со stop < key.
     * Возвращает индекс первого элемента, где stop >= key.
     */
    private int lowerBoundEnd(Segment[] arr, int key) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid].stop < key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo; // ровно 'lo' элементов имеют stop < key
    }

    // ═══════════════════════════════════════════════════════════════
    // 🔹 КЛАСС ОТРЕЗКА
    // ═══════════════════════════════════════════════════════════════

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Гарантируем start <= stop даже при "перевёрнутом" вводе
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            return Integer.compare(this.start, o.start);
        }
    }
}