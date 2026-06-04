package by.it.group551001.teryokhin.lesson04;

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
            //System.out.println(a[i]);
        }

        int[] buffer = new int[n];
        for(int size = 1;size < n;size *= 2)
        {
            for(int i = 0;i + size < n;i += size + size)
            {
                // [i, i + size - 1] and [i + size, Math.min(i + size + size - 1, n - 1)]

                int i1 = i;
                int i2 = i + size;
                int i_end = Math.min(i + size + size - 1, n - 1);
                int i_dest = 0;

                while(i1 <= i + size - 1 && i2 <= i_end)buffer[i_dest++] = a[i1] <= a[i2] ? a[i1++] : a[i2++];
                while(i1 <= i + size - 1)buffer[i_dest++] = a[i1++];
                while(i2 <= i_end)buffer[i_dest++] = a[i2++];

                System.arraycopy(buffer, 0, a, i, i_dest);
            }
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
