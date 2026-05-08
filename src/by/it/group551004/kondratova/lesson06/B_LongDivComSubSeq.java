package by.it.group551004.kondratova.lesson06;

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
Наибольшая кратная подпоследовательность
Чтение данных — считывается размер массива и сам массив натуральных чисел.
Инициализация — создаётся массив dp длиной n, заполненный единицами (каждый элемент сам по себе — последовательность длины 1).
Внешний цикл (по i от 1 до n-1) — текущий элемент как потенциальный конец подпоследовательности.
Внутренний цикл (по j от 0 до i-1) — перебирает все предыдущие элементы.
Проверка делимости — если m[i] делится нацело на m[j] (m[i] % m[j] == 0), то последовательность можно продолжить.
Обновление dp[i] — dp[i] = max(dp[i], dp[j] + 1).
Обновление максимума — отслеживается наибольшее значение в dp.
Возврат результата — максимальное значение в dp — длина наибольшей кратной подпоследовательности.
*/

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // dp[i] - длина наибольшей кратной подпоследовательности, заканчивающейся на m[i]
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // каждый элемент сам по себе образует последовательность длины 1

        int maxLength = 1; // минимальная длина = 1

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                // условие: m[i] делится на m[j] (m[i] % m[j] == 0)
                if (m[i] % m[j] == 0) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }
}