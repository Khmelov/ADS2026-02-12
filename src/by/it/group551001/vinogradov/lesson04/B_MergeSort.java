package by.it.group551001.vinogradov.lesson04;

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
    void merge(int[] s, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] left = new int[n1]; int[] right = new int[n2];
        for (int i = 0; i < n1; i++) {
            left[i] = s[l + i];
        }
        for (int i = 0; i < n2; i++) {
            right[i] = s[m + 1 + i];
        }
        int i = 0;
        int j = 0;
        int k = l;
        while ((i < n1) && (j < n2)) {
            if (left[i] < right[j]) {
                s[k] = left[i];
                i++;
            }
            else {
                s[k] = right[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            s[k] = left[i];
            i++;
            k++;
        }
        while (j < n2) {
            s[k] = right[j];
            j++;
            k++;
        }
    }

    void mergeSort(int[] s, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(s, l, m);
            mergeSort(s, m + 1, r);
            merge(s, l, m, r);
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
        mergeSort(a, 0, n-1);
        return a;
    }
}
