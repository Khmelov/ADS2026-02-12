package by.it.group551001.Barusevich.lesson01.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int w = scanner.nextInt();
        int n = scanner.nextInt();
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        boolean[] dp = new boolean[w + 1];
        dp[0] = true;

        for (int i = 0; i <= w; i++) {
            if (dp[i]) {
                for (int j = 0; j < n; j++) {
                    if (i + gold[j] <= w) {
                        dp[i + gold[j]] = true;
                    }
                }
            }
        }

        int result = 0;
        for (int i = w; i >= 0; i--) {
            if (dp[i]) {
                result = i;
                break;
            }
        }

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}