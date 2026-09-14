package by.it.group551003.sysoev.lesson04;

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

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        mergeSort(a, 0, n-1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    private void merge(int[] arr, int l, int m, int r){
        int L[] = new int[m-l+1];
        int R[] = new int[r-m];
        if (m + 1 - l >= 0) System.arraycopy(arr, l, L, 0, m + 1 - l);
        if (r - m >= 0) System.arraycopy(arr, m+1, R, 0, r - m);
        int i = 0, j = 0;
        while (i < m-l+1 && j < r-m){
            if (L[i] < R[j]){
                arr[i+j+l] = L[i];
                i++;
            }
            else{
                arr[i+j+l] = R[j];
                j++;
            }
        }
        while (i < m-l+1){
            arr[i+j+l] = L[i];
            i++;
        }
        while (j<r-m){
            arr[i+j+l] = R[j];
            j++;
        }
    }
    private void mergeSort(int[] arr, int l, int r){
        if (l<r) {
            int m = (r + l) / 2;

            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);

            merge(arr, l, m, r);
        }
    }


}
