package by.it.group551004.yakhnin.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int stairs[] = new int[n + 1]; // +1 для удобства, stairs[0] = 0
        stairs[0] = 0; // стоимость начала (перед первой ступенькой)
        for (int i = 1; i <= n; i++) {
            stairs[i] = scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        if (n == 0) return 0;

        int[] dp = new int[n + 1];
        dp[0] = 0; // перед первой ступенькой сумма = 0
        dp[1] = stairs[1]; // до первой ступеньки можно дойти только с шага 0

        for (int i = 2; i <= n; i++) {
            dp[i] = stairs[i] + Math.max(dp[i-1], dp[i-2]);
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