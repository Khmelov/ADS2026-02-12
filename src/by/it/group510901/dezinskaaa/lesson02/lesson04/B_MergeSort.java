package by.it.group510901.dezinskaaa.lesson02.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
реализуйте сортировку слиянием для одномерного массива.
сложность алгоритма должна быть не хуже, чем O(n log n)

первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
необходимо отсортировать полученный массив.

sample input:
5
2 3 9 2 9
sample output:
2 2 3 9 9
*/
public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    // рекурсивная сортировка слиянием
    private void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    // слияние двух отсортированных частей
    private void merge(int[] arr, int left, int mid, int right) {
        // размеры временных массивов
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // создаём временные массивы
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        // копируем данные
        for (int i = 0; i < n1; i++)
            leftArr[i] = arr[left + i];
        for (int j = 0; j < n2; j++)
            rightArr[j] = arr[mid + 1 + j];

        // слияние
        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        // копируем оставшиеся элементы
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }
        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     начало задачи     !!!!!!!!!!!!!!!!!!!!!!!!!

        // размер массива
        int n = scanner.nextInt();
        // сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // сортировка слиянием
        if (n > 0) {
            mergeSort(a, 0, n - 1);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     конец задачи     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
}