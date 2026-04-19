package by.it.group551002.savitsky.lesson04;

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

    int[] sortArray(int[] arrA) {

        if (arrA == null)
            return null;

        if (arrA.length < 2)
            return arrA;

        int mid = arrA.length / 2;
        int[] arrB = new int[mid];
        System.arraycopy(arrA, 0, arrB, 0, mid);

        int[] arrC = new int[arrA.length - mid];
        System.arraycopy(arrA, arrA.length / 2, arrC, 0, arrA.length - mid);

        arrB = sortArray(arrB);
        arrC = sortArray(arrC);

        return mergeSort(arrB, arrC);
    }

    int[] mergeSort(int[] arrA, int[] arrB) {
        int[] arrC = new int[arrA.length + arrB.length];
        int posA = 0;
        int posB = 0;

        for (int i = 0; i < arrC.length; i++) {
            if (posA == arrA.length) {
                arrC[i] = arrB[posB];
                ++posB;

            } else if (posB == arrB.length) {
                arrC[i] = arrA[posA];
                ++posA;

            } else if (arrA[posA] < arrB[posB]) {
                arrC[i] = arrA[posA];
                ++posA;

            } else {
                arrC[i] = arrB[posB];
                ++posB;
            }
        }
        return arrC;
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
        return sortArray(a);
    }
}
