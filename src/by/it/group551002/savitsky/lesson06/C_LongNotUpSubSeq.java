package by.it.group551002.savitsky.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

    private int search(int[] d, int len, int value) {
        int left = 0;
        int right = len;

        while (left < right) {
            int mid = (left + right + 1) / 2;
            if (d[mid] >= value) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] d = new int[n + 1];
        int[] parent = new int[n];
        int[] tailIdx = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            d[i] = Integer.MIN_VALUE;
        }
        d[0] = Integer.MAX_VALUE;

        Arrays.fill(parent, -1);

        int maxLen = 0;

        for (int i = 0; i < n; i++) {
            int value = m[i];

            int j = search(d, maxLen, value);

            int newLen = j + 1;

            d[newLen] = value;
            tailIdx[newLen] = i;

            if (j > 0) {
                parent[i] = tailIdx[j];
            }

            if (newLen > maxLen) {
                maxLen = newLen;
            }
        }

        int[] answer = new int[maxLen];
        int currentIdx = tailIdx[maxLen];

        for (int i = maxLen - 1; i >= 0; i--) {
            answer[i] = currentIdx + 1;
            currentIdx = parent[currentIdx];
        }

        System.out.println(maxLen);
        for (int i = 0; i < maxLen; i++) {
            System.out.print(answer[i] + " ");
        }
        System.out.println();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return maxLen;
    }
}
