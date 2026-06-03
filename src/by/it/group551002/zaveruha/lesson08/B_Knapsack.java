package by.it.group551002.zaveruha.lesson08;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt(); // Вместимость рюкзака
        int n = scanner.nextInt(); // Количество слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i] будет хранить максимальный вес, который можно собрать в рюкзак вместимостью i
        int[] dp = new int[W + 1];

        // Перебираем каждый слиток по одному разу
        for (int i = 0; i < n; i++) {
            // Идем по рюкзаку ОБРАТНО (от W до веса текущего слитка)
            // Это гарантирует, что каждый слиток мы используем не более одного раза
            for (int currW = W; currW >= gold[i]; currW--) {
                // Если мы возьмем этот слиток, станет ли веса больше, чем было до него?
                dp[currW] = Math.max(dp[currW], dp[currW - gold[i]] + gold[i]);
            }
        }

        int result = dp[W];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
