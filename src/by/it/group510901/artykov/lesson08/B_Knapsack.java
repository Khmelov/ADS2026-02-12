package by.it.group510901.artykov.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак без повторов

Первая строка входа содержит целые числа
    1<=W<=100000     вместимость рюкзака
    1<=n<=300        число золотых слитков
                    (каждый можно использовать только один раз).
Следующая строка содержит n целых чисел, задающих веса каждого из слитков:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

Найдите методами динамического программирования
максимальный вес золота, который можно унести в рюкзаке.

Sample Input:
10 3
1 4 8
Sample Output:
9
*/

public class B_Knapsack {

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
         * который можно получить
         * при вместимости i
         */
        int[] dp = new int[w + 1];

        /*
         * Перебираем слитки
         */
        for (int i = 0; i < n; i++) {

            /*
             * Идём справа налево,
             * чтобы не использовать
             * один слиток повторно
             */
            for (int j = w; j >= gold[i]; j--) {

                dp[j] = Math.max(
                        dp[j],
                        dp[j - gold[i]] + gold[i]
                );
            }
        }

        return dp[w];
    }

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                B_Knapsack.class.getResourceAsStream("dataB.txt");

        B_Knapsack instance = new B_Knapsack();

        int res = instance.getMaxWeight(stream);

        System.out.println(res);
    }
}