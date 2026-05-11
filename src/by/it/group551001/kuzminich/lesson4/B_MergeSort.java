package by.it.group551001.kuzminich.lesson4;

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
    private int[] merge_sort ( int[] from_arr, int[] to_arr, int left, int right) {

        if (left == right) {
            to_arr[left] = from_arr[left];
            return to_arr; // базовый случай
        }
        int mid = left + (right - left) / 2;
        int[] left_arr = merge_sort(from_arr, to_arr, left, mid);
        int[] right_arr = merge_sort(from_arr, to_arr, mid + 1, right);
        int target[];
        if (left_arr == from_arr) {
            target = to_arr;
        } else {
            target = from_arr;
        }

        int left_curr = left;
        int right_curr = mid + 1;

        for (int i = left; i <= right; i++) {
            if (left_curr <= mid && right_curr <= right) {
                if (left_arr[left_curr] < right_arr[right_curr]) {
                    target[i] = left_arr[left_curr];
                    left_curr++;
                } else {
                    target[i] = right_arr[right_curr];
                    right_curr++;
                }
            } else if (left_curr <= mid) {
                target[i] = left_arr[left_curr];
                left_curr++;
            } else {
                target[i] = right_arr[right_curr];
                right_curr++;
            }

        }
        return target;
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
        int[] temp = new int[n];
        int[] result = merge_sort ( a, temp, 0, n-1);

        System.arraycopy(result, 0, a,0,n);
        return a;






        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    }
}
