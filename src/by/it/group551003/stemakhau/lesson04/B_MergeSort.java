package by.it.group551003.stemakhau.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        int[] result = instance.getMergeSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Читаем массив (без лишнего вывода)
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Сортировка слиянием (рекурсивная версия)
        mergeSort(a, 0, n - 1);

        return a;
    }

    // Рекурсивная сортировка слиянием
    private void mergeSort(int[] a, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Сортируем левую и правую половины
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);

            // Сливаем отсортированные половины
            merge(a, left, mid, right);
        }
    }

    // Слияние двух отсортированных половин
    private void merge(int[] a, int left, int mid, int right) {
        // Размеры временных массивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Создаём временные массивы
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Копируем данные
        for (int i = 0; i < n1; i++) {
            leftArray[i] = a[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = a[mid + 1 + j];
        }

        // Слияние
        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                a[k] = leftArray[i];
                i++;
            } else {
                a[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Копируем оставшиеся элементы
        while (i < n1) {
            a[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            a[k] = rightArray[j];
            j++;
            k++;
        }
    }
}