package by.it.group551004.kapusta.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // длина последовательности
        int n = scanner.nextInt();
        int[] a = new int[n];

        // читаем массив
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // dp[i] = длина максимальной невозрастающей подпоследовательности, заканчивающейся в i
        int[] dp = new int[n];

        // parent[i] = индекс предыдущего элемента в подпоследовательности
        int[] parent = new int[n];

        int bestLen = 1;
        int bestEnd = 0;

        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            parent[i] = -1;

            for (int j = 0; j < i; j++) {
                if (a[j] >= a[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                }
            }

            if (dp[i] > bestLen) {
                bestLen = dp[i];
                bestEnd = i;
            }
        }

        // восстановление ответа
        int[] seq = new int[bestLen];
        int pos = bestLen - 1;
        int cur = bestEnd;

        while (cur != -1) {
            seq[pos--] = cur + 1; // индексация с 1
            cur = parent[cur];
        }

        // вывод
        System.out.println(bestLen);
        for (int i = 0; i < bestLen; i++) {
            System.out.print(seq[i] + " ");
        }

        return bestLen;
    }
}
