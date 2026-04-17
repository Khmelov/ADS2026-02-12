package by.it.group551001.docenko.lesson04;

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

        this.mergeSort(a, 0, a.length-1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    void mergeSort(int[] arr, int l, int r) {
        if (r == l) {
            return;
        }

        int m = (l + r) / 2;

        this.mergeSort(arr, l, m);
        this.mergeSort(arr, m+1, r);

        int[] temp = arr.clone();

        int l_cur = l, r_cur = m + 1;
        for (int i = l; i <= r; i++) {
            if (l_cur <= m && r_cur <= r)
            {
                if (temp[l_cur] <= temp[r_cur])
                {
                    arr[i] = temp[l_cur];
                    l_cur++;
                }
                else
                {
                    arr[i] = temp[r_cur];
                    r_cur++;
                }
            }
            else if (l_cur <= m)
            {
                arr[i] = temp[l_cur];
                l_cur++;
            }
            else
            {
                arr[i] = temp[r_cur];
                r_cur++;
            }
        }

    }

}
