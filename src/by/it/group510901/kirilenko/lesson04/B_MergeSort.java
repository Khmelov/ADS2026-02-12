package by.it.group510901.kirilenko.lesson04;

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
        int[] temp = new int[n];
        for (int width = 1; width < n; width *= 2) {
            for (int i = 0; i < n; i += 2 * width) {
                int left = i;
                int mid = Math.min(i + width - 1, n - 1);
                int right = Math.min(i + 2 * width - 1, n - 1);
                for (int j = left; j <= right; j++) {
                    temp[j] = a[j];
                }
                int p1 = left;
                int p2 = mid + 1;
                int p = left;
                while (p1 <= mid && p2 <= right) {
                    if (temp[p1] <= temp[p2]) {
                        a[p++] = temp[p1++];
                    } else {
                        a[p++] = temp[p2++];
                    }
                }
                while (p1 <= mid) {
                    a[p++] = temp[p1++];
                }
                while (p2 <= right) {
                    a[p++] = temp[p2++];
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
