package by.it.group551004.podvitelskiymichael.lesson05;

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

        //сортируем отрезки
        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];

            int left = 0, right = n - 1;
            int pos = -1;

            while (left <= right) {
                int mid = (left + right) / 2;
                if (segments[mid].start <= p) {
                    pos = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            int count = 0;

            // проверяем только подходящие отрезки
            for (int j = 0; j <= pos; j++) {
                if (segments[j].stop >= p) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    // QuickSort
    void quickSort(Segment[] arr, int left, int right) {
        if (left >= right) return;

        Segment pivot = arr[(left + right) / 2];
        int i = left, j = right;

        while (i <= j) {
            while (arr[i].compareTo(pivot) < 0) i++;
            while (arr[j].compareTo(pivot) > 0) j--;

            if (i <= j) {
                Segment temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }

        quickSort(arr, left, j);
        quickSort(arr, i, right);
    }

    // отрезок
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
            // сортировка по началу, при равенстве — по концу
            if (this.start != o.start)
                return this.start - o.start;
            return this.stop - o.stop;
        }
    }
}