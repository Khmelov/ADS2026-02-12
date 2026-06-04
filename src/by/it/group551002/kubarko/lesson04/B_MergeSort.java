package by.it.group551002.kubarko.lesson04;

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



        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }
    void mergeSort(int arr[], int l, int r)
    {

        if (l < r)
        {
            int m = l + (r - l) / 2;

            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);

            merge(arr, l, m, r);
        }
    }

    void merge(int arr[], int l, int m, int r)
    {
        int sizeL = m-l+1;
        int sizeR = r-m;

        int[] L = new int[sizeL];
        int[] R = new int[sizeR];

        for (int i = 0; i < sizeL; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < sizeR; ++j)
            R[j] = arr[m + 1 + j];

        int i = 0, j = 0;

        int write = l;
        while (i < sizeL && j < sizeR)
        {
            if(L[i] <= R[j])
            {
                arr[write] = L[i];
                i++;
            }
            else
            {
                arr[write] = R[j];
                j++;
            }
            write++;
        }

        while(i < sizeL)
        {
            arr[write] = L[i];
            i++;
            write++;
        }

        while(j < sizeR)
        {
            arr[write] = R[j];
            j++;
            write++;
        }

    }
}
