package by.it.group551002.litovchenko.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
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
        // long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        // long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        // !!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // размер массива
        int n = scanner.nextInt();
        // сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }
        int[] buff = new int[n];

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием

        a = merge_sort(a, buff, 0, n - 1);

        // !!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    int[] merge_sort(int[] up, int[] down, int left, int right) {
        if (left == right) {
            down[left] = up[left];
            return down;
        }
        int mid = (right + left) / 2;

        merge_sort(up, down, left, mid);
        merge_sort(up, down, mid + 1, right);

        int[] left_arr = Arrays.copyOf(down, Array.getLength(down));
        int[] right_arr = Arrays.copyOf(down, Array.getLength(down));

        int i = left, j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (left_arr[i] <= right_arr[j]) {
                down[k] = left_arr[i];
                i++;
            } else {
                down[k] = right_arr[j];
                j++;
            }
            k++;

        }
        while (i <= mid) {
            down[k] = left_arr[i];
            k++;
            i++;
        }

        while (j <= right) {
            down[k] = right_arr[j];
            k++;
            j++;
        }

        return down;
    }

}
