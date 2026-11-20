package by.it.group551003.kardash.lesson08;

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

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();   // количество вариантов слитков
        int[] weights = new int[n];

        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }

        // DP массив: dp[w] = максимальный вес, который можно получить для вместимости w
        int[] dp = new int[W + 1];

        // Инициализируем dp[0] = 0 (для пустого рюкзака)
        dp[0] = 0;

        // Заполняем массив dp
        for (int capacity = 1; capacity <= W; capacity++) {
            dp[capacity] = 0;  // начинаем с 0
            for (int i = 0; i < n; i++) {
                if (weights[i] <= capacity) {
                    // Пробуем положить слиток weights[i] в рюкзак
                    dp[capacity] = Math.max(dp[capacity],
                            dp[capacity - weights[i]] + weights[i]);
                }
            }
        }

        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}