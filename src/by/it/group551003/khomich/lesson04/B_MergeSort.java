package by.it.group551003.khomich.lesson04;

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
        for (int blockSize = 1; blockSize < n; blockSize *= 2) {
            for (int blockStart = 0; blockStart < n - blockSize; blockStart += 2 * blockSize) {
                int leftBorder = blockStart;
                int midBorder = blockStart + blockSize;
                int rightBorder = blockStart + 2 * blockSize;
                if (rightBorder > n) rightBorder = n;

                int[] sorted = new int[rightBorder - leftBorder];
                int leftIdx = 0, rightIdx = 0;

                while (leftBorder + leftIdx < midBorder && midBorder + rightIdx < rightBorder) {
                    if (a[leftBorder + leftIdx] <= a[midBorder + rightIdx]) {
                        sorted[leftIdx + rightIdx] = a[leftBorder + leftIdx];
                        leftIdx++;
                    } else {
                        sorted[leftIdx + rightIdx] = a[midBorder + rightIdx];
                        rightIdx++;
                    }
                }
                while (leftBorder + leftIdx < midBorder) {
                    sorted[leftIdx + rightIdx] = a[leftBorder + leftIdx];
                    leftIdx++;
                }
                while (midBorder + rightIdx < rightBorder) {
                    sorted[leftIdx + rightIdx] = a[midBorder + rightIdx];
                    rightIdx++;
                }
                for (int i = 0; i < leftIdx + rightIdx; i++) {
                    a[leftBorder + i] = sorted[i];
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}
