package by.it.group551001.Barusevich.lesson01.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

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

        for (int i = 0; i < n; i++) {
            for (int j = w; j >= gold[i]; j--) {
                if (dp[j - gold[i]]) {
                    dp[j] = true;
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
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}