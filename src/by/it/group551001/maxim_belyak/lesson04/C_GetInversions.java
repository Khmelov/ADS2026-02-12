package by.it.group551001.maxim_belyak.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        // Считываем результат и выводим его
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        if (!scanner.hasNextInt()) return 0;
        int n = scanner.nextInt();

        // Читаем сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Запускаем модифицированную сортировку слиянием
        // Используем long для хранения результата, так как инверсий может быть много
        return (int) mergeSortAndCount(a, 0, n - 1);
    }

    // Рекурсивный метод: делит массив и суммирует инверсии
    private long mergeSortAndCount(int[] a, int left, int right) {
        long count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Считаем инверсии в левой половине
            count += mergeSortAndCount(a, left, mid);
            // Считаем инверсии в правой половине
            count += mergeSortAndCount(a, mid + 1, right);
            // Считаем инверсии, возникающие между половинами
            count += mergeAndCount(a, left, mid, right);
        }
        return count;
    }

    // Метод слияния двух отсортированных половин с подсчетом инверсий
    private long mergeAndCount(int[] a, int left, int mid, int right) {
        // Копируем временные массивы
        int[] leftArr = Arrays.copyOfRange(a, left, mid + 1);
        int[] rightArr = Arrays.copyOfRange(a, mid + 1, right + 1);

        int i = 0, j = 0, k = left;
        long internalInversions = 0;

        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                // Если левый элемент меньше или равен — инверсии нет
                a[k++] = leftArr[i++];
            } else {
                // Если правый элемент меньше левого — это инверсия!
                // Он меньше текущего левого И ВСЕХ остальных элементов в leftArr
                a[k++] = rightArr[j++];
                internalInversions += (leftArr.length - i);
            }
        }

        // Копируем оставшиеся элементы
        while (i < leftArr.length) {
            a[k++] = leftArr[i++];
        }
        while (j < rightArr.length) {
            a[k++] = rightArr[j++];
        }

        return internalInversions;
    }
}