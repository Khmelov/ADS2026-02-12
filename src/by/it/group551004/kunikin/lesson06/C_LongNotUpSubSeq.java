package by.it.group551004.kunikin.lesson06;

import java.io.FileNotFoundException;
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
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] tails = new int[n];
        int[] tailsIndices = new int[n];
        int[] prev = new int[n];
        int len = 0;

        for (int i = 0; i < n; i++) {
            int x = m[i];
            int low = 0;
            int high = len - 1;
            int replaceIdx = -1;

            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (tails[mid] < x) {
                    replaceIdx = mid;
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }

            if (replaceIdx == -1) {
                tails[len] = x;
                tailsIndices[len] = i;
                prev[i] = (len > 0) ? tailsIndices[len - 1] : -1;
                len++;
            } else {
                tails[replaceIdx] = x;
                tailsIndices[replaceIdx] = i;
                prev[i] = (replaceIdx > 0) ? tailsIndices[replaceIdx - 1] : -1;
            }
        }

        int[] resultIndices = new int[len];
        int curr = tailsIndices[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            resultIndices[i] = curr + 1;
            curr = prev[curr];
        }

        System.out.println(len);
        for (int i = 0; i < len; i++) {
            System.out.print(resultIndices[i] + (i == len - 1 ? "" : " "));
        }
        System.out.println();
        int result = len;
        return result;
    }

}