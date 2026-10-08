package by.it.group510902.klimantovich.lesson01.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача: Отсортировать массив чисел методом слияния
Принцип работы (Разделяй и властвуй):
1. Рекурсивно делим массив пополам, пока не получим массивы из 1 элемента
2. Сливаем отсортированные половинки обратно в один массив

Сложность: O(n log n) в любом случае (даже на отсортированных данных)
*/
public class B_MergeSort {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        int[] result = instance.getMergeSort(stream);

        // Выводим отсортированный массив
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    public int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива и сам массив
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Запускаем сортировку, если массив больше 1 элемента
        if (n > 1) {
            int[] temp = new int[n];  // временный массив для слияния
            mergeSort(a, temp, 0, n - 1);
        }

        return a;
    }

    /*
     * Рекурсивная сортировка слиянием
     * Принцип:
     * - Если массив из одного элемента, он уже отсортирован
     * - Иначе делим пополам, сортируем каждую половину, потом сливаем
     *
     * Пример: [2,3,9,2,9]
     * Разделение: [2,3,9] и [2,9]
     *   [2,3,9] -> [2,3] и [9]
     *     [2,3] -> [2] и [3] (базовый случай)
     *     Слияние [2] и [3] -> [2,3]
     *   Слияние [2,3] и [9] -> [2,3,9]
     *   [2,9] -> [2] и [9] -> слияние -> [2,9]
     * Слияние [2,3,9] и [2,9] -> [2,2,3,9,9]
     */
    private void mergeSort(int[] arr, int[] temp, int left, int right) {
        if (left < right) {  // пока есть что делить
            int mid = left + (right - left) / 2;  // находим середину

            // Рекурсивно сортируем левую и правую половины
            mergeSort(arr, temp, left, mid);
            mergeSort(arr, temp, mid + 1, right);

            // Сливаем две отсортированные половины
            merge(arr, temp, left, mid, right);
        }
    }

    /*
     * Слияние двух отсортированных половин
     * Принцип: как в карточной игре - берем меньший элемент из двух куч
     *
     * Пример: слияние [2,3,9] и [2,9]
     * i=0(2), j=0(2) -> берем 2 из правой (индекс j) -> [2]
     * i=0(2), j=1(9) -> берем 2 из левой -> [2,2]
     * i=1(3), j=1(9) -> берем 3 из левой -> [2,2,3]
     * i=2(9), j=1(9) -> берем 9 из левой -> [2,2,3,9]
     * i=3(конец), j=1(9) -> берем 9 из правой -> [2,2,3,9,9]
     */
    private void merge(int[] arr, int[] temp, int left, int mid, int right) {
        // Копируем все элементы во временный массив
        for (int i = left; i <= right; i++) {
            temp[i] = arr[i];
        }

        int i = left;      // указатель на левую половину
        int j = mid + 1;   // указатель на правую половину
        int k = left;      // указатель на результирующий массив

        // Сливаем: берем меньший элемент из двух половин
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k] = temp[i];  // берем из левой
                i++;
            } else {
                arr[k] = temp[j];  // берем из правой
                j++;
            }
            k++;
        }

        // Копируем остатки из левой половины (если есть)
        while (i <= mid) {
            arr[k] = temp[i];
            i++;
            k++;
        }

        // Копируем остатки из правой половины (если есть)
        while (j <= right) {
            arr[k] = temp[j];
            j++;
            k++;
        }
    }
}