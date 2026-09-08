package by.it.group551003.ogonovsky.lesson08;

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

        // d[c] - максимальный вес золота, помещающийся в рюкзак вместимости c
        int[] d = new int[W + 1];
        // d[0] = 0 по умолчанию

        // обрабатываем каждый слиток ровно один раз;
        // обратный обход по c гарантирует, что слиток i не используется дважды
        for (int i = 0; i < n; i++) {
            int w = gold[i];
            if (w == 0 || w > W) continue;
            for (int c = W; c >= w; c--) {
                int candidate = d[c - w] + w;
                if (candidate > d[c]) {
                    d[c] = candidate;
                }
            }
        }

        return d[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}