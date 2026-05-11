package by.it.group551003.stemakhau.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

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

    public static int upperBound(int[] arr, int len, int value) {
        int low = 0;
        int high = len;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] >= value) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] dp = new int[n];
        int[] pos = new int[n];
        int[] prev = new int[n];
        Arrays.fill(prev, -1);

        int length = 0;

        for (int i = 0; i < n; ++i) {
            int j = upperBound(dp, length, m[i]);
            dp[j] = m[i];
            pos[j] = i;
            prev[i] = (j > 0) ? pos[j - 1] : -1;
            if (j == length) {
                length++;
            }
        }

        int[] indices = new int[length];
        int idx = pos[length - 1];
        for (int k = length - 1; k >= 0; k--) {
            indices[k] = idx;
            idx = prev[idx];
        }

        for (int i : indices) System.out.print(i + 1 + " ");

        return length;
    }

}