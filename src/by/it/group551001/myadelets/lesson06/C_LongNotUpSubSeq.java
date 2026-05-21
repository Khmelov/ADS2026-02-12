package by.it.group551001.myadelets.lesson06;

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
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        if (n == 0) {
            return 0;
        }

        int[] tails = new int[n + 1];
        int[] pos = new int[n + 1];
        int[] prev = new int[n];

        int bestLen = 0;

        for (int i = 0; i < n; i++) {
            int x = a[i];

            int left = 1;
            int right = bestLen;
            int resPos = bestLen + 1;

            while (left <= right) {
                int mid = (left + right) / 2;
                if (tails[mid] < x) {
                    resPos = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }

            int len = resPos;
            prev[i] = (len > 1) ? pos[len - 1] : -1;

            tails[len] = x;
            pos[len] = i;

            if (len > bestLen) {
                bestLen = len;
            }
        }

        int[] answerIdx = new int[bestLen];
        int cur = pos[bestLen];
        int ptr = bestLen - 1;
        while (cur != -1) {
            answerIdx[ptr--] = cur + 1;
            cur = prev[cur];
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bestLen; i++) {
            if (i > 0) sb.append(' ');
            sb.append(answerIdx[i]);
        }
        System.out.println();
        System.out.print(sb.toString());

        return bestLen;
    }

}