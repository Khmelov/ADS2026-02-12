package by.it.group510901.filimonova.lesson01.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Задача на программирование: наибольшая кратная подпоследовательность

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] делится на предыдущий
    т.е. для всех 1<=j<k, A[i[j+1]] делится на A[i[j]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    4
    3 6 7 12

    Sample Output:
    3
*/

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] arr = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        // Решение методом динамического программирования O(n^2)
        int result = longestDivisibleSubsequence(arr);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int longestDivisibleSubsequence(int[] arr) {
        int n = arr.length;
        if (n == 0) return 0;
        if (n == 1) return 1;

        // Сначала сортируем массив для удобства
        // (чтобы гарантировать, что если число делится на другое,
        // то оно больше или равно ему)
        int[] sorted = arr.clone();
        Arrays.sort(sorted);

        // dp[i] - длина наибольшей кратной подпоследовательности,
        // заканчивающейся на элементе i
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // каждая подпоследовательность длины хотя бы 1

        int maxLength = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // Проверяем, делится ли текущий элемент на предыдущий
                if (sorted[i] % sorted[j] == 0) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }
}