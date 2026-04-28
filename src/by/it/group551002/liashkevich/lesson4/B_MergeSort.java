package by.it.group551002.liashkevich.lesson4;

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
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        mergeSort(a, 0, n-1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    void merge(int[] Arr, int Left, int Median, int Right) {
        int i, j, k;
        int n1 = Median - Left + 1;
        int n2 = Right - Median;

        int[] leftArr = new int [n1];
        int[] rightArr = new int[n2];

        for (i = 0; i < n1; i++)
            leftArr[i] = Arr[Left + i];
        for (j = 0; j < n2; j++)
            rightArr[j] = Arr[Median + 1 + j];

        i = 0;
        j = 0;
        k = Left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                Arr[k] = leftArr[i];
                i++;
            }
            else {
                Arr[k] = rightArr[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            Arr[k] = leftArr[i];
            i++;
            k++;
        }

        while (j < n2) {
            Arr[k] = rightArr[j];
            j++;
            k++;
        }
    }

    void mergeSort(int[] Arr, int Left, int Right) {
        if (Left < Right) {
            int Median = Left + (Right - Left) / 2;
            mergeSort(Arr, Left, Median);
            mergeSort(Arr, Median + 1, Right);
            merge(Arr, Left, Median, Right);
        }
    }
}

