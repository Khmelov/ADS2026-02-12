package by.it.group551004.kapusta.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream ) {
        Scanner scanner = new Scanner(stream);

        int W = scanner.nextInt();   // вместимость рюкзака
        int n = scanner.nextInt();   // количество слитков

        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[w] = максимальный вес, который можно набрать при вместимости w
        int[] dp = new int[W + 1];

        // 0/1 knapsack — идём по весам назад!
        for (int g : gold) {
            for (int w = W; w >= g; w--) {
                dp[w] = Math.max(dp[w], dp[w - g] + g);
            }
        }

        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
