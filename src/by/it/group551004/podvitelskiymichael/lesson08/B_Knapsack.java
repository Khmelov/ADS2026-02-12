package by.it.group551004.podvitelskiymichael.lesson08;

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

        int[] dp = new int[w + 1];

        for (int i = 0; i < n; i++) {
            for (int c = w; c >= gold[i]; c--) {
                dp[c] = Math.max(dp[c], dp[c - gold[i]] + gold[i]);
            }
        }

        return dp[w];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}