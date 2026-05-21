package by.it.group551002.sikorski.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }
        a = mergeSort(a, 0, n - 1);
        // тут ваше решение (реализуйте сортировку слиянием)

        // https://ru.wikipedia.org/wiki/Сортировка_слиянием

        return a;
    }
    private int[] mergeSort(int[] a, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            merge(a, left, mid, right);
        }
        return a;
    }

    // Слияние двух отсортированных частей
    private void merge(int[] a, int left, int mid, int right) {
        int[] leftArray = new int[mid - left + 1];
        int[] rightArray = new int[right - mid];

        // Копируем данные во временные массивы
        for (int i = 0; i < leftArray.length; i++)
            leftArray[i] = a[left + i];
        for (int j = 0; j < rightArray.length; j++)
            rightArray[j] = a[mid + 1 + j];

        int i = 0, j = 0;
        int k = left;

        // Слияние
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                a[k] = leftArray[i];
                i++;
            } else {
                a[k] = rightArray[j];
                j++;
            }
            k++;
        }

        // Дозаписываем остатки, если они есть
        while (i < leftArray.length) {
            a[k] = leftArray[i];
            i++;
            k++;
        }
        while (j < rightArray.length) {
            a[k] = rightArray[j];
            j++;
            k++;
        }
    }

}
