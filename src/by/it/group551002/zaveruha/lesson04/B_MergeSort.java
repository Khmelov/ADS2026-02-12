package by.it.group551002.zaveruha.lesson04;

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
        class Helper {
            void mergeSort(int left, int right) {
                if (left >= right) return;
                int mid = (left + right) / 2;
                mergeSort(left, mid);
                mergeSort(mid + 1, right);

                // слияние
                int n1 = mid - left + 1;
                int n2 = right - mid;
                int[] L = new int[n1];
                int[] R = new int[n2];
                for (int i = 0; i < n1; i++) L[i] = a[left + i];
                for (int i = 0; i < n2; i++) R[i] = a[mid + 1 + i];
                int i = 0, j = 0, k = left;
                while (i < n1 && j < n2) {
                    if (L[i] <= R[j]) a[k++] = L[i++];
                    else a[k++] = R[j++];
                }
                while (i < n1) a[k++] = L[i++];
                while (j < n2) a[k++] = R[j++];
            }
        }

        new Helper().mergeSort(0, n - 1);



        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
