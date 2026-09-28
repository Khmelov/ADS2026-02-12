package by.it.group551003.kalach.lesson04;

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
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызываем рекурсивную сортировку
        mergeSort(a, 0, n - 1);

        return a;
    }

    private void mergeSort(int[] a, int left, int right) {
        if (left < right) {
            // Находим середину
            int mid = left + (right - left) / 2;

            // Рекурсивно сортируем левую и правую половины
            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);

            // Сливаем отсортированные части
            merge(a, left, mid, right);
        }
    }

    private void merge(int[] a, int left, int mid, int right) {
        // Создаем временные массивы для копирования данных
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) L[i] = a[left + i];
        for (int j = 0; j < n2; j++) R[j] = a[mid + 1 + j];

        // Индексы для прохода по подмассивам и основному массиву
        int i = 0, j = 0, k = left;

        // Сравниваем элементы из L и R и кладем меньший в основной массив
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                a[k++] = L[i++];
            } else {
                a[k++] = R[j++];
            }
        }

        // Докладываем остатки, если они есть
        while (i < n1) a[k++] = L[i++];
        while (j < n2) a[k++] = R[j++];
    }


}
