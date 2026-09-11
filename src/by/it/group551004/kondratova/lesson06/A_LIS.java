package by.it.group551004.kondratova.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Задача на программирование: наибольшая возрастающая подпоследовательность
см.     https://ru.wikipedia.org/wiki/Задача_поиска_наибольшей_увеличивающейся_подпоследовательности
        https://en.wikipedia.org/wiki/Longest_increasing_subsequence

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    где каждый элемент A[i[k]] больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]<A[i[j+1]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    1 3 3 2 6

    Sample Output:
    3
        Находит длину самой длинной подпоследовательности,
    в которой каждый следующий элемент строго больше предыдущего.
Чтение данных — считывается размер массива и сам массив чисел.
Инициализация — создаётся массив dp такой же длины, как исходный. Каждый элемент dp[i] инициализируется единицей, потому что любая последовательность из одного элемента имеет длину 1.
Внешний цикл (по i от 1 до конца массива) — рассматривает каждый элемент как возможный конец подпоследовательности.
Внутренний цикл (по j от 0 до i-1) — перебирает все элементы, которые стоят перед текущим.
Проверка условия — если элемент m[j] меньше m[i], то его можно поставить перед m[i] в возрастающей последовательности.
Обновление dp[i] — dp[i] становится максимальным из текущего значения и dp[j] + 1 (длина последовательности, заканчивающейся на m[j], плюс текущий элемент).
Обновление максимума — после обработки каждого i проверяется, не стал ли dp[i] больше глобального максимума.
Возврат результата — глобальный максимум и есть длина наибольшей возрастающей подпоследовательности.

*/

public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // dp[i] - длина наибольшей возрастающей подпоследовательности, заканчивающейся на m[i]
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // инициализируем: каждый элемент сам по себе образует НВП длины 1

        int maxLength = 1; // минимальная длина НВП = 1

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (m[j] < m[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLength = Math.max(maxLength, dp[i]);
        }

        return maxLength;
    }
}