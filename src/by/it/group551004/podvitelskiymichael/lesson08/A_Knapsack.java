package by.it.group551004.podvitelskiymichael.lesson08;

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

        int[] dp = new int[w + 1];

        for (int c = 1; c <= w; c++) {
            for (int i = 0; i < n; i++) {
                if (gold[i] <= c) {
                    dp[c] = Math.max(dp[c], dp[c - gold[i]] + gold[i]);
                }
            }
        }

        return dp[w];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}