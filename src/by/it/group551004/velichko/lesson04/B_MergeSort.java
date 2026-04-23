package by.it.group551004.velichko.lesson04;

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
        // size — размер сливаемых подмассивов (начинаем с 1)
        for (int size = 1; size < n; size *= 2) {

            // left — начало левого подмассива
            for (int left = 0; left < n - size; left += 2 * size) {
                int mid = left + size - 1;                    // конец левого подмассива
                int right = Math.min(left + 2 * size - 1, n - 1);  // конец правого подмассива

                int[] temp = new int[right - left + 1];
                int i = left;
                int j = mid + 1;
                int k = 0;
                while (i <= mid && j <= right) {
                    if (a[i] <= a[j]) {
                        temp[k++] = a[i++];
                    } else {
                        temp[k++] = a[j++];
                    }
                }
                while (i <= mid) {
                    temp[k++] = a[i++];
                }
                while (j <= right) {
                    temp[k++] = a[j++];
                }
                for (int t = 0; t < temp.length; t++) {
                    a[left + t] = temp[t];
                }
            }
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
