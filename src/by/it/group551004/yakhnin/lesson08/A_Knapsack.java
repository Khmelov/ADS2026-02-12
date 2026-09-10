package by.it.group551004.yakhnin.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();      // вместимость рюкзака
        int n = scanner.nextInt();      // количество типов слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        int[] dp = new int[W + 1];
        dp[0] = 0;  // для пустого рюкзака вес 0

        //заполяем  для всех вместимостей от 1 до W
        for (int w = 1; w <= W; w++) {
            int max = 0;
            // пробуем каждый слиток
            for (int i = 0; i < n; i++) {
                if (gold[i] <= w) {
                    // кладем его + оптимально заполняем остаток
                    int candidate = gold[i] + dp[w - gold[i]];
                    if (candidate > max) {
                        max = candidate;
                    }
                }
            }
            dp[w] = max;
        }

        scanner.close();
        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}