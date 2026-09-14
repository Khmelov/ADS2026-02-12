package by.it.group551001.kondratiev.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return mergeSort(a);
    }

    private int[] mergeSort(int[] arr){
        if (arr.length == 1) return arr;
        int pivot = arr.length / 2;
        int[] left = mergeSort(Arrays.copyOfRange(arr, 0, pivot));
        int[] right = mergeSort(Arrays.copyOfRange(arr, pivot, arr.length));

        return merge(left, right);
    }

    private int[] merge(int[] l, int[] r){
        int i, j, k;
        i = j = k = 0;

        int[] res = new int[l.length + r.length];

        while (i < l.length && j < r.length){
            if (l[i] < r[j] ) res[k++] = l[i++];
            else res[k++] = r[j++];
        }

        while (i < l.length) res[k++] = l[i++];
        while (j < r.length) res [k++] = r[j++];

        return res;
    }

}
