package by.it.group510901.filimonova.lesson01.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] stairs = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            stairs[i] = scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        if (n == 0) return 0;
        if (n == 1) return stairs[1];

        // dp[i] - максимальная сумма для достижения ступеньки i
        int[] dp = new int[n + 1];

        // На нулевой ступеньке (начальная позиция) сумма = 0
        dp[0] = 0;
        // На первую ступеньку можно попасть только с нулевой (прыжок на 1)
        dp[1] = stairs[1];
        // На вторую ступеньку можно попасть:
        // - с нулевой (прыжок на 2): 0 + stairs[2]
        // - с первой (прыжок на 1): dp[1] + stairs[2]
        dp[2] = Math.max(stairs[2], dp[1] + stairs[2]);

        // Заполняем для остальных ступенек
        for (int i = 3; i <= n; i++) {
            // На ступеньку i можно попасть с i-1 или i-2
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i];
        }

        int result = dp[n];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}
/*dp[i] — максимальная сумма для достижения ступеньки i

dp[0] = 0 — начальная позиция

dp[1] = stairs[1] — только прыжок с 0

dp[2] = max(stairs[2], dp[1] + stairs[2]) — с 0 или с 1

dp[i] = max(dp[i-1], dp[i-2]) + stairs[i] для i >= 3

Сложность: O(n)*/