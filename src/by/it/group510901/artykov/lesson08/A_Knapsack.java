package by.it.group510901.artykov.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак с повторами

Первая строка входа содержит целые числа
    1<=W<=100000     вместимость рюкзака
    1<=n<=300        сколько есть вариантов золотых слитков
                     (каждый можно использовать множество раз).
Следующая строка содержит n целых чисел, задающих веса слитков:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

Найдите методами динамического программирования
максимальный вес золота, который можно унести в рюкзаке.
*/

public class A_Knapsack {

    int getMaxWeight(InputStream stream ) {

        Scanner scanner = new Scanner(stream);

        // Вместимость рюкзака
        int w = scanner.nextInt();

        // Количество слитков
        int n = scanner.nextInt();

        // Массив слитков
        int[] gold = new int[n];

        for (int i = 0; i < n; i++) {

            gold[i] = scanner.nextInt();
        }

        /*
         * dp[i] =
         * максимальный вес,
         * который можно набрать
         * для вместимости i
         */
        int[] dp = new int[w + 1];

        /*
         * Перебираем все веса
         * от 1 до W
         */
        for (int i = 1; i <= w; i++) {

            /*
             * Проверяем каждый слиток
             */
            for (int j = 0; j < n; j++) {

                /*
                 * Если слиток помещается
                 */
                if (gold[j] <= i) {

                    dp[i] = Math.max(
                            dp[i],
                            dp[i - gold[j]] + gold[j]
                    );
                }
            }
        }

        return dp[w];
    }

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                A_Knapsack.class.getResourceAsStream("dataA.txt");

        A_Knapsack instance = new A_Knapsack();

        int res = instance.getMaxWeight(stream);

        System.out.println(res);
    }
}