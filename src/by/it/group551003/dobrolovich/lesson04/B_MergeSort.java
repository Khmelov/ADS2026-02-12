package by.it.group551003.dobrolovich.lesson04;

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

    void sort(int a[], int left, int right){
        if (left >= right) return;
        int mid = left + (right - left)/2;
        sort (a, left, mid);
        sort (a, mid + 1, right);
        merge(a, left, right);
    }

    void merge(int a[], int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        int L[] = new int[mid - left + 1];
        int R[] = new int[right - mid];

        for (int i = 0; i < L.length; i++) L[i] = a[i + left];
        for (int i = 0; i < R.length; i++) R[i] = a[i + mid + 1];

        int li = 0, ri = 0, i = left;
        while (li < L.length || ri < R.length) {
            if (li < L.length && ri < R.length) {
                if (L[li] <= R[ri]) a[i++] =  L[li++];
                else a[i++] = R[ri++];
            }
            else if (li < L.length) a[i++] =  L[li++];
            else a[i++] =  R[ri++];
        }
    }

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
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        sort(a, 0, a.length - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
