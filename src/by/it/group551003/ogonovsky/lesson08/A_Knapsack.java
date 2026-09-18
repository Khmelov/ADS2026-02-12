package by.it.group551003.ogonovsky.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();
        int n = scanner.nextInt();
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // d[c] - максимальный вес золота, помещающийся в рюкзак вместимости c
        int[] d = new int[W + 1];
        // d[0] = 0 по умолчанию

        for (int c = 1; c <= W; c++) {
            int best = 0;
            for (int i = 0; i < n; i++) {
                int w = gold[i];
                if (w > 0 && w <= c) {
                    int candidate = d[c - w] + w;
                    if (candidate > best) {
                        best = candidate;
                    }
                }
            }
            d[c] = best;
        }

        return d[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}