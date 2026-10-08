package by.it.group510902.paleychik.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
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

        // Чтение данных
        if (!scanner.hasNextInt()) return new int[0];
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызов рекурсивной сортировки
        return sort(a);
    }

    // Рекурсивное разделение массива
    private int[] sort(int[] arr) {
        if (arr.length <= 1) {
            return arr;
        }

        int mid = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);

        // Рекурсивно делим и потом сливаем
        return merge(sort(left), sort(right));
    }

    // Слияние двух отсортированных массивов в один
    private int[] merge(int[] left, int[] right) {
        int[] res = new int[left.length + right.length];
        int i = 0; // индекс для left
        int j = 0; // индекс для right
        int k = 0; // индекс для итогового массива res

        // Сравниваем элементы и перекладываем меньший
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                res[k] = left[i];
                i = i + 1;
            } else {
                res[k] = right[j];
                j = j + 1;
            }
            k = k + 1;
        }

        while (i < left.length) {
            res[k] = left[i];
            i = i + 1;
            k = k + 1;
        }

        while (j < right.length) {
            res[k] = right[j];
            j = j + 1;
            k = k + 1;
        }

        return res;
    }
}