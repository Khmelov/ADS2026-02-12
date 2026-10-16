package by.it.group510902.Tarnogurskaya.lesson04;

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
        int[] result = instance.getMergeSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    private void mergeSort(int[] arr, int left, int right) {
        if (left >= right) { // Базовый случай: если остался 1 элемент или пустой отрезок
            return; // массив из 1 элемента уже отсортирован
        }
        int mid = left + (right - left) / 2; // Находим середину
        mergeSort(arr, left, mid);  // Рекурсивно сортируем левую половину
        mergeSort(arr, mid + 1, right); // Рекурсивно сортируем правую половину
        merge(arr, left, mid, right);  // Сливаем две отсортированные половины
    }
    //Сортировка слиянием = РАЗДЕЛИ до единичных элементов + СЛИВАЙ по правилу "меньший берём первым"
    private void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1]; // Создаём временный массив для результата слияния
        int i = left;  // указатель на левую половину (от left до mid)
        int j = mid + 1; // указатель на правую половину (от mid+1 до right)
        int k = 0;  // указатель во временном массиве
        // СЛИЯНИЕ: сравниваем элементы и берём меньший
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++]; // берём из левой половины
            } else {
                temp[k++] = arr[j++]; // берём из правой половины
            }
        }
        // ДОЗАПОЛНЕНИЕ: если в левой половине остались элементы
        while (i <= mid) {
            temp[k++] = arr[i++];
        }
        // ДОЗАПОЛНЕНИЕ: если в правой половине остались элементы
        while (j <= right) {
            temp[k++] = arr[j++];
        }
        // КОПИРОВАНИЕ: переносим отсортированные элементы обратно
        for (int p = 0; p < temp.length; p++) {
            arr[left + p] = temp[p];
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        mergeSort(a, 0, n - 1);

        return a;
    }
}