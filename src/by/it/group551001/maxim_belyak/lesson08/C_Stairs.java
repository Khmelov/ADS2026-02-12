package by.it.group551001.maxim_belyak.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int stairs[] = new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Базовые случаи
        if (n == 0) return 0;
        if (n == 1) return stairs[0];

        // Создаем DP массив, где dp[i] - максимальная сумма для достижения i-й ступеньки
        int[] dp = new int[n];

        // Начальные значения
        dp[0] = stairs[0];                    // с 0 на 1 ступеньку
        dp[1] = Math.max(stairs[0] + stairs[1], stairs[1]);  // с 0 на 2 ступеньку или через 1

        // Заполняем для остальных ступенек
        for (int i = 2; i < n; i++) {
            // На i-ю ступеньку можно попасть:
            // 1) с (i-1)-й ступеньки (шаг на 1)
            // 2) с (i-2)-й ступеньки (шаг на 2)
            dp[i] = Math.max(dp[i-1], dp[i-2]) + stairs[i];
        }

        int result = dp[n-1];

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