package by.it.group551001.bogush.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
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
        Merge_sort(a,0,a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    void Merge_sort(int[] arr,int left,int right) {
        if (left >= right)
            return;
        int mid = (left + right) / 2;

        Merge_sort(arr,left,mid);
        Merge_sort(arr,mid+1,right);

        Merge(arr,left,mid,right);
    }

    void Merge(int[] arr,int left,int mid,int right) {

        int[] left_arr = Arrays.copyOfRange(arr,left,mid + 1);
        int[] right_arr = Arrays.copyOfRange(arr,mid + 1,right + 1);
        int k = left;
        int i = 0;
        int j = 0;

        while (i < left_arr.length && j < right_arr.length) {
            if (left_arr[i] <= right_arr[j]) {
                arr[k] = left_arr[i];
                k++;
                i++;
            } else {
                arr[k] = right_arr[j];
                k++;
                j++;
            }
        }

        while (i < left_arr.length) {
            arr[k] = left_arr[i];
            k++;
            i++;
        }
        while (j < right_arr.length) {
            arr[k] = right_arr[j];
            k++;
            j++;
        }
    }
}
