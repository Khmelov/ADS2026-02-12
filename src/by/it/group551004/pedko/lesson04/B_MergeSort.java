package by.it.group551004.pedko.lesson04;

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
        long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    public static int[] merge(int[] array1, int[] array2) {
        int i;
        int j;
        int index;
        int size1;
        int size2;
        int answerSize;
        int[] answerArray;

        i = 0;
        j = 0;
        index = 0;
        size1 = 0;
        size2 = 0;
        answerSize = 0;

        size1 = array1.length;
        size2 = array2.length;

        answerSize = size1 + size2;
        answerArray = new int[answerSize];

        while (i < size1 && j < size2) {
            if (array1[i] <= array2[j]) {
                answerArray[index] = array1[i];
                i++;
            } else {
                answerArray[index] = array2[j];
                j++;
            }
            index++;
        }

        while (i < size1) {
            answerArray[index] = array1[i];
            i++;
            index++;
        }

        while (j < size2) {
            answerArray[index] = array2[j];
            j++;
            index++;
        }

        return answerArray;
    }

    int[] mergesort(int[] up, int[] down, int left, int right) {

        if (left == right) {
            down[left] = up[left];
            down = new int[1];
            down[0] = up[left];
            return down;
        }

        int middle = left + (right - left) / 2;

        int[] leftBuffer = mergesort(up, down, left, middle);
        int[] rightBuffer = mergesort(up, down, middle + 1, right);

        int[] target;
        if (leftBuffer == up) {
            target = down;
        } else {
            target = up;
        }

        return merge(leftBuffer, rightBuffer);
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

        int[] buff;
        buff = new int[a.length];

        a = mergesort(a, buff, 0, a.length - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
