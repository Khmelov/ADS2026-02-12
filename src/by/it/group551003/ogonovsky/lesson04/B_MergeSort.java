package by.it.group551003.ogonovsky.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        int[] result = instance.getMergeSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // запускаем сортировку слиянием на всём диапазоне [0, n-1]
        if (n > 1) {
            int[] buffer = new int[n];
            mergeSort(a, buffer, 0, n - 1);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    // рекурсивно делим массив пополам, сортируем половины, затем сливаем
    private void mergeSort(int[] a, int[] buffer, int left, int right) {
        if (left >= right) return;
        int mid = (left + right) >>> 1;
        mergeSort(a, buffer, left, mid);
        mergeSort(a, buffer, mid + 1, right);
        merge(a, buffer, left, mid, right);
    }

    // слияние двух отсортированных подотрезков a[left..mid] и a[mid+1..right]
    private void merge(int[] a, int[] buffer, int left, int mid, int right) {
        int i = left;      // указатель по левой половине
        int j = mid + 1;   // указатель по правой половине
        int k = left;      // указатель по буферу

        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                buffer[k++] = a[i++];
            } else {
                buffer[k++] = a[j++];
            }
        }
        while (i <= mid) {
            buffer[k++] = a[i++];
        }
        while (j <= right) {
            buffer[k++] = a[j++];
        }

        // копируем обратно в исходный массив
        for (int t = left; t <= right; t++) {
            a[t] = buffer[t];
        }
    }
}