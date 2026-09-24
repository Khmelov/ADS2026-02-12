package by.it.group510901.petsevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

        quickSort(segments, 0, segments.length - 1);

        // Для каждого отрезка создаём массив начал и концов
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        int[] stopsSorted = stops.clone();
        quickSortInt(stopsSorted, 0, stopsSorted.length - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Находим количество отрезков, у которых начало <= point
            int leftCount = countLessOrEqual(starts, point);

            // Находим количество отрезков, у которых конец < point
            int rightCount = countLess(stopsSorted, point);

            // Количество отрезков, содержащих точку =
            // отрезки с началом <= point минус отрезки с концом < point
            result[i] = leftCount - rightCount;
        }

        return result;
    }

    // Быстрая сортировка для массива Segment
    private void quickSort(Segment[] arr, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(arr, left, right);
            quickSort(arr, left, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, right);
        }
    }

    private int partition(Segment[] arr, int left, int right) {
        Segment pivot = arr[(left + right) / 2];
        while (left <= right) {
            while (arr[left].compareTo(pivot) < 0) left++;
            while (arr[right].compareTo(pivot) > 0) right--;
            if (left <= right) {
                Segment temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
                left++;
                right--;
            }
        }
        return left;
    }

    private void quickSortInt(int[] arr, int left, int right) {
        if (left < right) {
            int pivotIndex = partitionInt(arr, left, right);
            quickSortInt(arr, left, pivotIndex - 1);
            quickSortInt(arr, pivotIndex + 1, right);
        }
    }

    private int partitionInt(int[] arr, int left, int right) {
        int pivot = arr[(left + right) / 2];
        while (left <= right) {
            while (arr[left] < pivot) left++;
            while (arr[right] > pivot) right--;
            if (left <= right) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
                left++;
                right--;
            }
        }
        return left;
    }

    private int countLessOrEqual(int[] arr, int x) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] <= x) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result + 1;
    }

    private int countLess(int[] arr, int x) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] < x) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result + 1;
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
            // Сортируем по началу отрезка
            return Integer.compare(this.start, o.start);
        }
    }
}