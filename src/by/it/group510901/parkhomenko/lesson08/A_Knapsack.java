package by.it.group510901.parkhomenko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Решение задачи A: рюкзак с повторениями слитков.
public class A_Knapsack {

    // Считывает вместимость и веса, затем возвращает максимальный достижимый вес.
    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int capacity = scanner.nextInt();
        int n = scanner.nextInt();
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        int[] dp = new int[capacity + 1];

        // dp[w] - лучший вес, который можно набрать при вместимости w.
        for (int w = 1; w <= capacity; w++) {
            for (int weight : gold) {
                if (weight > 0 && weight <= w) {
                    dp[w] = Math.max(dp[w], dp[w - weight] + weight);
                }
            }
        }
        return dp[capacity];
    }

    // Запускает решение на dataA.txt.
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
