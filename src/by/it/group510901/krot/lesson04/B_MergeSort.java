package by.it.group510901.krot.lesson04;

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

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызываем сортировку
        mergeSort(a, 0, n - 1);

        return a;
    }

    //рекурсивная сортировка слиянием
    void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;

            //рекурсивно сортируем левую и правую части
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);

            //сливаем отсортированные части
            merge(array, left, middle, right);
        }
    }

    //слияние двух отсортированных частей
    void merge(int[] array, int left, int middle, int right) {
        //размеры временных массивов
        int sizeLeft = middle - left + 1;
        int sizeRight = right - middle;

        //временные массивы
        int[] leftArray = new int[sizeLeft];
        int[] rightArray = new int[sizeRight];

        //копируем данные
        for (int i = 0; i < sizeLeft; i++) {
            leftArray[i] = array[left + i];
        }
        for (int i = 0; i < sizeRight; i++) {
            rightArray[i] = array[middle + 1 + i];
        }

        //сливаем обратно
        int i = 0, j = 0, k = left;
        while (i < sizeLeft && j < sizeRight) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        //копируем оставшиеся элементы
        while (i < sizeLeft) {
            array[k] = leftArray[i];
            i++;
            k++;
        }
        while (j < sizeRight) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }


}
