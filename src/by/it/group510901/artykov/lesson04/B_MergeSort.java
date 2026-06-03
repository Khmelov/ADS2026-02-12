package by.it.group510901.artykov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Сортировка слиянием (Merge Sort).
 *
 * Идея алгоритма:
 * 1. Делим массив на две части
 * 2. Сортируем каждую часть отдельно
 * 3. Объединяем части обратно
 *
 * Сложность:
 * O(n log n)
 */

public class B_MergeSort {

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                B_MergeSort.class.getResourceAsStream("dataB.txt");

        B_MergeSort instance = new B_MergeSort();

        int[] result = instance.getMergeSort(stream);

        // Вывод отсортированного массива
        for (int value : result) {

            System.out.print(value + " ");
        }
    }

    /*
     * Метод считывает массив
     * и запускает сортировку.
     */
    int[] getMergeSort(InputStream stream)
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

        // Запуск сортировки
        mergeSort(a, 0, a.length - 1);

        return a;
    }

    /*
     * Рекурсивная сортировка массива.
     *
     * left  - левая граница
     * right - правая граница
     */
    void mergeSort(int[] a, int left, int right) {

        // Если массив состоит из 1 элемента
        if (left >= right) {

            return;
        }

        // Находим середину
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
     * два отсортированных массива.
     */
    void merge(int[] a, int left, int mid, int right) {

        // Размер левой части
        int leftSize = mid - left + 1;

        // Размер правой части
        int rightSize = right - mid;

        // Временные массивы
        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];

        // Копируем левую часть
        for (int i = 0; i < leftSize; i++) {

            leftArray[i] = a[left + i];
        }

        // Копируем правую часть
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
         * Сравниваем элементы
         * и записываем меньший
         */
        while (i < leftSize && j < rightSize) {

            if (leftArray[i] <= rightArray[j]) {

                a[k] = leftArray[i];

                i++;

            } else {

                a[k] = rightArray[j];

                j++;
            }

            k++;
        }

        /*
         * Добавляем оставшиеся элементы
         * из левой части
         */
        while (i < leftSize) {

            a[k] = leftArray[i];

            i++;
            k++;
        }

        /*
         * Добавляем оставшиеся элементы
         * из правой части
         */
        while (j < rightSize) {

            a[k] = rightArray[j];

            j++;
            k++;
        }
    }
}