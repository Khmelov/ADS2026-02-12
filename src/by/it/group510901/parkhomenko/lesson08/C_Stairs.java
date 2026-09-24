package by.it.group510901.parkhomenko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Решение задачи C: максимальная сумма при подъеме по лестнице.
public class C_Stairs {

    // Считывает веса ступенек и возвращает максимальную сумму на пути до верхней ступеньки.
    int getMaxSum(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] stairs = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            stairs[i] = scanner.nextInt();
        }

        int[] dp = new int[n + 1];
        dp[1] = stairs[1];

        // dp[i] - максимальная сумма, с которой можно попасть на i-ю ступеньку.
        for (int i = 2; i <= n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i];
        }
        return dp[n];
    }

    // Запускает решение на dataC.txt.
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}
