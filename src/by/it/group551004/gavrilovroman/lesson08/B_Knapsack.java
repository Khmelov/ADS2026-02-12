package by.it.group551004.gavrilovroman.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();
        int n = scanner.nextInt();
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }
        boolean[] dp = new boolean[W + 1];
        dp[0] = true;
        int result = 0;

        for (int i = 0; i < n; i++) {
            int g = gold[i];
            for (int weight = W; weight >= g; weight--) {
                if (dp[weight - g]) {
                    dp[weight] = true;
                    if (weight > result) {
                        result = weight;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
