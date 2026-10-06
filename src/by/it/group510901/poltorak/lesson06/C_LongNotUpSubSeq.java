package by.it.group510901.poltorak.lesson06;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозростающая подпоследовательность

Дано:
    целое число 1<=n<=1E5 ( ОБРАТИТЕ ВНИМАНИЕ НА РАЗМЕРНОСТЬ! )
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] не больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]>=A[i[j+1]].

    В первой строке выведите её длину k,
    во второй - её индексы i[1]<i[2]<…<i[k]
    соблюдая A[i[1]]>=A[i[2]]>= ... >=A[i[n]].

    (индекс начинается с 1)

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    5 3 4 4 2

    Sample Output:
    4
    1 3 4 5
*/


public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int[] tail = new int[n];
        int[] prevIndex = new int[n];
        Arrays.fill(prevIndex, -1);
        int[] posInTail = new int[n];

        int length = 0;

        for (int i = 0; i < n; i++) {
            int x = a[i];
            int left = 0;
            int right = length;

            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tail[mid] < x) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            int pos = left;
            if (pos > 0) {
                prevIndex[i] = posInTail[pos - 1];
            }
            tail[pos] = x;
            posInTail[pos] = i;
            if (pos == length) {
                length++;
            }
        }
        int[] resultIndices = new int[length];
        int currentPos = posInTail[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            resultIndices[i] = currentPos + 1;
            currentPos = prevIndex[currentPos];
        }
        System.out.println(length);
        for (int idx : resultIndices) {
            System.out.print(idx + " ");
        }
        System.out.println();

        return length;
    }

}