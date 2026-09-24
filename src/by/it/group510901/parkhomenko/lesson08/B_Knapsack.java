package by.it.group510901.parkhomenko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Решение задачи B: рюкзак без повторений слитков.
public class B_Knapsack {

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

        // Идем по вместимости справа налево, чтобы каждый слиток использовался не больше одного раза.
        for (int weight : gold) {
            for (int w = capacity; w >= weight; w--) {
                dp[w] = Math.max(dp[w], dp[w - weight] + weight);
            }
        }
        return dp[capacity];
    }

    // Запускает решение на dataB.txt.
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
