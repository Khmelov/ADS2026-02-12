package by.it.group510901.dremluk.lesson08;

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


Sample Input:
10 3
1 4 8
Sample Output:
10

Sample Input 2:

15 3
2 8 16
Sample Output 2:
14

*/

public class A_Knapsack {
/*суть задачи состоит в том, что у нас есть рюкзак фиксированной вместимости и n типов золотых слитков. нам нужно набрать */
    int getMaxWeight(InputStream stream ) {
/*максимально возможный вес, не превышая вместимость рюкзака,
 слитки можно использовать повторно. пробуем добавить каждый тип слитка и, если он помещается, добавляем к общему весу,*/
        Scanner scanner = new Scanner(stream);
/*таким образом заполняем массив, после чего
выбираем максимальный вес из всех вариантов. направление обхода слева направо (от веса слитка до веса рюкзака)*/
        int w = scanner.nextInt();   // вместимость рюкзака
        int n = scanner.nextInt();   // количество типов слитков

        int[] gold = new int[n];     // веса слитков
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i] — максимальный вес, который можно набрать при вместимости i
        int[] dp = new int[w + 1];
        dp[0] = 0; // при весе 0 ничего не берём

        // перебираем все возможные веса рюкзака
        for (int i = 1; i <= w; i++) {

            // пробуем каждый слиток
            for (int j = 0; j < n; j++) {

                // если слиток помещается
                if (gold[j] <= i) {

                    // выбираем лучший вариант: взять этот слиток или не брать
                    dp[i] = Math.max(dp[i], dp[i - gold[j]] + gold[j]);
                }
            }
        }

        return dp[w]; // максимальный вес при полной вместимости
    }


    public static void main(String[] args) throws FileNotFoundException {

        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();

        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}