package by.it.group510901.sinyak.lesson04;

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

        int len = a.length;

        int[] result = new int[len];

        for (int size = 1; size < len; size *= 2) {
            for (int left = 0; left < len - size; left += 2 * size) {

                int mid = left + size;
                int right = Math.min(left + 2 * size, len);

                for (int i = left; i < right; i++) {
                    result[i] = a[i];
                }

                int i = left;      // Указатель левой части в result
                int j = mid;       // Указатель правой части в result

                for (int k = left; k < right; k++) {
                    if (i >= mid) {           // Левая часть закончилась
                        a[k] = result[j++];
                    } else if (j >= right) {  // Правая часть закончилась
                        a[k] = result[i++];
                    } else if (result[i] <= result[j]) { // Сравниваем из черновика
                        a[k] = result[i++];
                    } else {
                        a[k] = result[j++];
                    }
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
