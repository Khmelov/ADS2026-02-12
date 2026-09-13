package by.it.group510901.artykov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Подсчёт количества инверсий в массиве.
 *
 * Инверсия —
 * это пара элементов:
 * i < j и A[i] > A[j]
 *
 * Для решения используется
 * сортировка слиянием.
 *
 * Сложность:
 * O(n log n)
 */

public class C_GetInversions {

    /*
     * Хранит количество инверсий.
     */
    private int inversionCount = 0;

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                C_GetInversions.class.getResourceAsStream("dataC.txt");

        C_GetInversions instance =
                new C_GetInversions();

        int result = instance.calc(stream);

        System.out.print(result);
    }

    /*
     * Метод считывает массив
     * и запускает подсчёт инверсий.
     */
    int calc(InputStream stream)
            throws FileNotFoundException {

        // Чтение данных
        Scanner scanner = new Scanner(stream);

        // Размер массива
        int n = scanner.nextInt();

        // Массив чисел
        int[] a = new int[n];

        // Заполнение массива
        for (int i = 0; i < n; i++) {

            a[i] = scanner.nextInt();
        }

        // Подсчёт инверсий
        mergeSort(a, 0, n - 1);

        return inversionCount;
    }

    /*
     * Рекурсивная сортировка массива.
     */
    void mergeSort(int[] a, int left, int right) {

        // Если массив состоит из 1 элемента
        if (left >= right) {

            return;
        }

        // Середина массива
        int mid = (left + right) / 2;

        // Сортировка левой части
        mergeSort(a, left, mid);

        // Сортировка правой части
        mergeSort(a, mid + 1, right);

        // Объединение частей
        merge(a, left, mid, right);
    }

    /*
     * Метод объединяет
     * две отсортированные части массива
     * и считает инверсии.
     */
    void merge(int[] a, int left, int mid, int right) {

        // Размер левой части
        int leftSize = mid - left + 1;

        // Размер правой части
        int rightSize = right - mid;

        // Временные массивы
        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];

        // Копирование левой части
        for (int i = 0; i < leftSize; i++) {

            leftArray[i] = a[left + i];
        }

        // Копирование правой части
        for (int i = 0; i < rightSize; i++) {

            rightArray[i] = a[mid + 1 + i];
        }

        // Индекс левого массива
        int i = 0;

        // Индекс правого массива
        int j = 0;

        // Индекс основного массива
        int k = left;

        /*
         * Сравнение элементов
         * и объединение массивов
         */
        while (i < leftSize && j < rightSize) {

            /*
             * Если элемент слева меньше,
             * инверсии нет
             */
            if (leftArray[i] <= rightArray[j]) {

                a[k] = leftArray[i];

                i++;

            } else {

                /*
                 * Если элемент справа меньше,
                 * значит оставшиеся элементы
                 * слева образуют инверсии
                 */
                a[k] = rightArray[j];

                inversionCount += leftSize - i;

                j++;
            }

            k++;
        }

        /*
         * Добавляем оставшиеся элементы
         * левого массива
         */
        while (i < leftSize) {

            a[k] = leftArray[i];

            i++;
            k++;
        }

        /*
         * Добавляем оставшиеся элементы
         * правого массива
         */
        while (j < rightSize) {

            a[k] = rightArray[j];

            j++;
            k++;
        }
    }
}