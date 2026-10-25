package by.it.group510901.artykov.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Даны число 1<=n<=100 ступенек лестницы и
целые числа −10000<=a[1],…,a[n]<=10000, которыми помечены ступеньки.
Найдите максимальную сумму, которую можно получить, идя по лестнице
снизу вверх (от нулевой до n-й ступеньки), каждый раз поднимаясь на
одну или на две ступеньки.
*/

public class C_Stairs {

    int getMaxSum(InputStream stream ) {

        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();

        int[] stairs = new int[n];

        for (int i = 0; i < n; i++) {

            stairs[i] = scanner.nextInt();
        }

        // =====================================================
        // Динамическое программирование
        // =====================================================

        /*
         * dp[i] =
         * максимальная сумма,
         * которую можно получить
         * до ступеньки i
         */
        int[] dp = new int[n];

        // Первая ступенька
        dp[0] = stairs[0];

        /*
         * Если ступенек больше одной
         */
        if (n > 1) {

            dp[1] = Math.max(
                    stairs[0] + stairs[1],
                    stairs[1]
            );
        }

        /*
         * Заполняем остальные значения
         */
        for (int i = 2; i < n; i++) {

            dp[i] = Math.max(
                    dp[i - 1],
                    dp[i - 2]
            ) + stairs[i];
        }

        return dp[n - 1];
    }

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                C_Stairs.class.getResourceAsStream("dataC.txt");

        C_Stairs instance = new C_Stairs();

        int res = instance.getMaxSum(stream);

        System.out.println(res);
    }
}