package by.it.group510901.parkhomenko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Задача A.
 * Нужно определить, скольким отрезкам принадлежит каждая точка.
 * Точка считается принадлежащей отрезку, если она лежит внутри него или на границе.
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
        int m = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int[] stops = new int[n];

        // Сохраняем отрезки и отдельно их правые границы.
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
            stops[i] = segments[i].stop;
        }

        // Отрезки сортируются по левой границе, а правые границы - отдельным массивом.
        quickSort(segments, 0, segments.length - 1);
        quickSort(stops, 0, stops.length - 1);

        int[] starts = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
        }

        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            int point = scanner.nextInt();

            // Подходящие отрезки: начались не позже точки и не закончились раньше точки.
            int started = upperBound(starts, point);
            int finishedBefore = lowerBound(stops, point);
            result[i] = started - finishedBefore;
        }
        return result;
    }

    private void quickSort(Segment[] segments, int left, int right) {
        if (left >= right) return;

        int i = left;
        int j = right;
        Segment pivot = segments[left + (right - left) / 2];

        // Разделяем массив на элементы меньше и больше опорного.
        while (i <= j) {
            while (segments[i].compareTo(pivot) < 0) i++;
            while (segments[j].compareTo(pivot) > 0) j--;
            if (i <= j) {
                Segment temp = segments[i];
                segments[i] = segments[j];
                segments[j] = temp;
                i++;
                j--;
            }
        }

        quickSort(segments, left, j);
        quickSort(segments, i, right);
    }

    private void quickSort(int[] values, int left, int right) {
        if (left >= right) return;

        int i = left;
        int j = right;
        int pivot = values[left + (right - left) / 2];

        while (i <= j) {
            while (values[i] < pivot) i++;
            while (values[j] > pivot) j--;
            if (i <= j) {
                int temp = values[i];
                values[i] = values[j];
                values[j] = temp;
                i++;
                j--;
            }
        }

        quickSort(values, left, j);
        quickSort(values, i, right);
    }

    private int lowerBound(int[] values, int target) {
        // Количество элементов, строго меньших target.
        int left = 0;
        int right = values.length;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (values[middle] < target) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }

    private int upperBound(int[] values, int target) {
        // Количество элементов, меньших либо равных target.
        int left = 0;
        int right = values.length;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (values[middle] <= target) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }

    private static class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // На случай, если границы придут в обратном порядке.
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment other) {
            int byStart = Integer.compare(start, other.start);
            return byStart != 0 ? byStart : Integer.compare(stop, other.stop);
        }
    }
}
