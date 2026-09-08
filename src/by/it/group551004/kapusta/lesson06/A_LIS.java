package by.it.group551004.kapusta.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // длина последовательности
        int n = scanner.nextInt();
        int[] a = new int[n];

        // читаем массив
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // dp[i] = длина наибольшей возрастающей подпоследовательности, заканчивающейся в i
        int[] dp = new int[n];

        int result = 1;

        for (int i = 0; i < n; i++) {
            dp[i] = 1; // минимальная длина — сам элемент

            for (int j = 0; j < i; j++) {
                if (a[j] < a[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }

            result = Math.max(result, dp[i]);
        }

        return result;
    }
}
