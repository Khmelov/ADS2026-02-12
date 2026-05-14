package by.it.group510901.dremluk.lesson08;

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
/*суть задачи состоит в том, что у нас есть рюкзак фиксированной вместимости и n типов золотых слитков*/
    int getMaxWeight(InputStream stream ) {
/* только здесь можно использовать какой-то
 тип слитка только один раз, направление обхода слева направо (от веса рюкзака до веса слитка)*/
        Scanner scanner = new Scanner(stream);
/**/
        int w = scanner.nextInt();   // вместимость рюкзака
        int n = scanner.nextInt();   // количество слитков

        int[] gold = new int[n];     // веса слитков
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[j] — максимальный вес при вместимости j
        // учитывая, что каждый слиток можно взять только один раз
        int[] dp = new int[w + 1];

        // перебираем каждый слиток
        for (int i = 0; i < n; i++) {

            // идём справа налево, чтобы не использовать слиток дважды
            for (int j = w; j >= gold[i]; j--) {

                // выбираем: взять этот слиток или не брать
                dp[j] = Math.max(dp[j], dp[j - gold[i]] + gold[i]);
            }
        }

        return dp[w]; // максимальный вес при полной вместимости
    }


    public static void main(String[] args) throws FileNotFoundException {

        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();

        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }

}