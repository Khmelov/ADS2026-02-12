package by.it.group551003.sukhodolskaya.lesson04;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        // Реализация сортировки слиянием
        mergeSort(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    /**
     * Рекурсивная функция сортировки слиянием
     * @param arr - сортируемый массив
     * @param left - левая граница отрезка
     * @param right - правая граница отрезка
     */
    private void mergeSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }

        //середина
        int mid = left + (right - left) / 2;

        //сортируем левую и правую половины
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);

        //сливаем две отсортированные половины
        merge(arr, left, mid, right);
    }

    /**
     * Слияние двух отсортированных частей массива
     * @param arr - исходный массив
     * @param left - начало левой части
     * @param mid - конец левой части
     * @param right - конец правой части
     */
    private void merge(int[] arr, int left, int mid, int right) {
        //временный массив для слияния
        int[] temp = new int[right - left + 1];

        int i = left;      // указатель на левую часть
        int j = mid + 1;   // указатель на правую часть
        int k = 0;         // указатель на временный массив

        //сравниваем элементы из обеих частей и копируем меньший
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }

        //копируем оставшиеся элементы левой части
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        //копируем правые
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        //обратно в исходный массив
        for (int p = 0; p < temp.length; p++) {
            arr[left + p] = temp[p];
        }
    }
}