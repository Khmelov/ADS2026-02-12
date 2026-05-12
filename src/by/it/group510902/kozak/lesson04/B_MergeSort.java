package by.it.group510902.kozak.lesson04;

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
        mergeSort(a, 0, a.length - 1);

        return a;
    }
        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        private void mergeSort(int[] a, int left, int right) {
            if (left >= right) return; // база рекурсии: 1 элемент — уже отсортирован

            int mid = left + (right - left) / 2;

            mergeSort(a, left, mid);
            mergeSort(a, mid + 1, right);
            merge(a, left, mid, right);
        }

// Слияние двух отсортированных подмассивов в один
        private void merge(int[] a, int left, int mid, int right) {
            int leftSize  = mid - left + 1;
            int rightSize = right - mid;


            int[] L = new int[leftSize];
            int[] R = new int[rightSize];

            for (int i = 0; i < leftSize; i++)  L[i] = a[left + i];
            for (int j = 0; j < rightSize; j++) R[j] = a[mid + 1 + j];

            int i = 0, j = 0;
            int k = left;


            while (i < leftSize && j < rightSize) {
                if (L[i] <= R[j]) {
                    a[k++] = L[i++];
                } else {
                    a[k++] = R[j++];
                }
            }


            while (i < leftSize)  a[k++] = L[i++];
            while (j < rightSize) a[k++] = R[j++];


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    }


}
