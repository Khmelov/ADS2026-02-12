package by.it.group551001.maxim_belyak.lesson05;

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

        // Сортировка на месте с 3-разбиением и устранением хвостовой рекурсии
        quickSort(segments, 0, n - 1);

        // Для каждой точки бинарный поиск + линейный проход
        for (int i = 0; i < m; i++) {
            result[i] = countSegments(segments, points[i]);
        }

        return result;
    }

    // Quicksort с 3-way partition и устранением хвостовой рекурсии
    private void quickSort(Segment[] arr, int lo, int hi) {
        while (lo < hi) {                           // хвостовая рекурсия → цикл
            int[] bounds = partition(arr, lo, hi);
            int lt = bounds[0], gt = bounds[1];

            // Рекурсивно обрабатываем меньшую половину
            // Большую — через цикл (экономим стек)
            if (lt - lo < hi - gt) {
                quickSort(arr, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSort(arr, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    // 3-way partition: делит на [< pivot] [== pivot] [> pivot]
    private int[] partition(Segment[] arr, int lo, int hi) {
        // Берём средний элемент как pivot (уменьшает шанс худшего случая)
        int mid = lo + (hi - lo) / 2;
        swap(arr, mid, lo);
        Segment pivot = arr[lo];

        int lt = lo;   // arr[lo..lt-1] < pivot
        int i  = lo + 1; // arr[lt..i-1]  == pivot
        int gt = hi;   // arr[gt+1..hi]  > pivot

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if      (cmp < 0) swap(arr, lt++, i++); // меньше → в левую зону
            else if (cmp > 0) swap(arr, i, gt--);   // больше → в правую зону
            else              i++;                   // равен → просто идём дальше
        }
        return new int[]{lt, gt};
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // Бинарный поиск первого отрезка где start <= point,
    // затем линейно влево считаем все где stop >= point
    private int countSegments(Segment[] segments, int point) {
        int lo = 0, hi = segments.length - 1, first = -1;

        // Бинарный поиск: самый правый индекс где start <= point
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (segments[mid].start <= point) {
                first = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        if (first == -1) return 0; // все отрезки начинаются после точки

        // Линейный проход влево — считаем подходящие отрезки
        int count = 0;
        for (int i = first; i >= 0; i--) {
            if (segments[i].stop >= point) {
                count++;
            }
        }
        return count;
    }

    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            // защита от обратного порядка концов
            this.start = Math.min(start, stop);
            this.stop  = Math.max(start, stop);
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            if (this.start != other.start) return Integer.compare(this.start, other.start);
            return Integer.compare(this.stop, other.stop);
        }
    }
}