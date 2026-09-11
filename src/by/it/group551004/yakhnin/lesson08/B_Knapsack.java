package by.it.group551004.yakhnin.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();      // вместимость рюкзака
        int n = scanner.nextInt();      // количество слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[w] = максимальный вес для вместимости w
        // Используем одномерный массив, но идем СПРАВА НАЛЕВО!
        int[] dp = new int[W + 1];

        // Перебираем каждый слиток
        for (int i = 0; i < n; i++) {
            // Идем от W вниз до gold[i] (чтобы не использовать слиток несколько раз)
            for (int w = W; w >= gold[i]; w--) {
                // Пробуем положить текущий слиток
                dp[w] = Math.max(dp[w], dp[w - gold[i]] + gold[i]);
            }
        }

        scanner.close();
        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}