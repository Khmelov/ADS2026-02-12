package by.it.group551004.kapusta.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // длина последовательности
        int n = scanner.nextInt();
        int[] a = new int[n];

        // читаем массив
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // dp[i] = длина максимальной подпоследовательности, заканчивающейся в i,
        // где каждый следующий элемент делится на предыдущий
        int[] dp = new int[n];

        int result = 1;

        for (int i = 0; i < n; i++) {
            dp[i] = 1; // минимальная длина — сам элемент

            for (int j = 0; j < i; j++) {
                if (a[i] % a[j] == 0) { // ключевое условие задачи
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }

            result = Math.max(result, dp[i]);
        }

        return result;
    }
}
