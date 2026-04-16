package by.it.group551004.kapusta.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream ) {
        Scanner scanner = new Scanner(stream);

        int W = scanner.nextInt();   // вместимость рюкзака
        int n = scanner.nextInt();   // количество типов слитков

        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[w] = максимальный вес, который можно набрать при вместимости w
        int[] dp = new int[W + 1];

        for (int w = 0; w <= W; w++) {
            for (int g : gold) {
                if (g <= w) {
                    dp[w] = Math.max(dp[w], dp[w - g] + g);
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
