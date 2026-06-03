package by.it.group510901.parkhomenko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Задача C.
 * Условие такое же, как в задаче A, но сортировка сделана устойчивее:
 * используется трехчастное разбиение и рекурсивно обрабатывается меньшая часть.
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
        int m = scanner.nextInt();
        int[] starts = new int[n];
        int[] stops = new int[n];

        // Для подсчета достаточно отдельно отсортировать начала и концы отрезков.
        for (int i = 0; i < n; i++) {
            int first = scanner.nextInt();
            int second = scanner.nextInt();
            starts[i] = Math.min(first, second);
            stops[i] = Math.max(first, second);
        }

        quickSort3(starts, 0, starts.length - 1);
        quickSort3(stops, 0, stops.length - 1);

        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            int point = scanner.nextInt();

            // start <= point учитывается через upperBound, stop < point - через lowerBound.
            result[i] = upperBound(starts, point) - lowerBound(stops, point);
        }
        return result;
    }

    private void quickSort3(int[] values, int left, int right) {
        // Цикл заменяет хвостовую рекурсию и уменьшает риск переполнения стека.
        while (left < right) {
            int pivot = values[left + (right - left) / 2];
            int lt = left;
            int i = left;
            int gt = right;

            // После разбиения: [left, lt) < pivot, [lt, gt] == pivot, (gt, right] > pivot.
            while (i <= gt) {
                if (values[i] < pivot) {
                    swap(values, lt++, i++);
                } else if (values[i] > pivot) {
                    swap(values, i, gt--);
                } else {
                    i++;
                }
            }

            // Рекурсивно сортируем меньшую часть, а большую продолжаем в цикле.
            if (lt - left < right - gt) {
                quickSort3(values, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort3(values, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(int[] values, int first, int second) {
        int temp = values[first];
        values[first] = values[second];
        values[second] = temp;
    }

    private int lowerBound(int[] values, int target) {
        // Возвращает количество элементов, строго меньших target.
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
        // Возвращает количество элементов, меньших либо равных target.
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
}
