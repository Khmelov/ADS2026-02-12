package by.it.group510901.petsevich.lesson05;

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

        int n = scanner.nextInt(); // число отрезков
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt(); // число точек
        int[] points = new int[m];
        int[] result = new int[m];

        // читаем отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            // если концы пришли в обратном порядке, исправляем
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            segments[i] = new Segment(start, stop);
        }

        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        quickSort3Way(segments, 0, segments.length - 1);

        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        for (int i = 0; i < m; i++) {
            int point = points[i];

            int firstIndex = findFirstSegment(starts, point);

            if (firstIndex == -1) {
                result[i] = 0;
                continue;
            }

            int count = 0;
            for (int j = firstIndex; j < n && segments[j].start <= point; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    private void quickSort3Way(Segment[] arr, int left, int right) {
        while (left < right) {
            // Выбираем опорный элемент (медиана из трех для улучшения производительности)
            Segment pivot = medianOfThree(arr, left, right);

            // 3-разбиение: [ < pivot | = pivot | > pivot ]
            int lt = left;  // граница меньших
            int gt = right; // граница больших
            int i = left;

            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(arr, lt, i);
                    lt++;
                    i++;
                } else if (cmp > 0) {
                    swap(arr, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }

            // Сортируем меньшую часть рекурсивно, а большую - итеративно
            if (lt - left < right - gt) {
                quickSort3Way(arr, left, lt - 1); // рекурсия на меньшей части
                left = gt + 1; // хвостовая рекурсия для большей части
            } else {
                quickSort3Way(arr, gt + 1, right); // рекурсия на меньшей части
                right = lt - 1; // хвостовая рекурсия для большей части
            }
        }
    }

    private Segment medianOfThree(Segment[] arr, int left, int right) {
        int mid = (left + right) / 2;
        if (arr[mid].compareTo(arr[left]) < 0)
            swap(arr, left, mid);
        if (arr[right].compareTo(arr[left]) < 0)
            swap(arr, left, right);
        if (arr[right].compareTo(arr[mid]) < 0)
            swap(arr, mid, right);
        swap(arr, mid, (left + right) / 2);
        return arr[(left + right) / 2];
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int findFirstSegment(int[] starts, int point) {
        int left = 0;
        int right = starts.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (starts[mid] <= point) {
                result = mid;
                left = mid + 1; // ищем дальше вправо, чтобы найти самый правый
            } else {
                right = mid - 1;
            }
        }

        // Находим первый подходящий отрезок (самый левый среди подходящих)
        if (result != -1) {
            while (result > 0 && starts[result - 1] <= point) {
                result--;
            }
        }

        return result;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // Сначала сравниваем по началу, если равны - по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }

        @Override
        public String toString() {
            return "[" + start + ", " + stop + "]";
        }
    }
}