package by.it.group551004.yurkovich.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        long startTime = System.currentTimeMillis();
        mergeSortCall(a);
        long finishTime = System.currentTimeMillis();
        System.out.println(finishTime - startTime);

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    public static void mergeSortCall(int[] arr) {
        int[] supArr = Arrays.copyOf(arr, arr.length);
        int left = 0;
        int right = arr.length - 1;
        mergeSort(arr, supArr, left, right);
    }

    public static void mergeSort(int[] arr, int[] supArr, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = left + (right - left) / 2;
        mergeSort(arr, supArr, left, mid);
        mergeSort(arr, supArr, mid + 1, right);
        merge(arr, supArr, left, mid, mid + 1, right);
    }

    public static void merge(int[] arr, int[] supArr, int left1, int right1, int left2, int right2) {
        System.arraycopy(arr, left1, supArr, left1, right2 + 1 - left1);
        int l = left1;
        int r = left2;
        for (int i = left1; i <= right2; i++) {
            if (l > right1) {
                arr[i] = supArr[r];
                r += 1;
            } else if (r > right2) {
                arr[i] = supArr[l];
                l += 1;
            } else if (supArr[l] < supArr[r]) {
                arr[i] = supArr[l];
                l += 1;
            } else {
                arr[i] = supArr[r];
                r += 1;
            }
        }
    }

}
