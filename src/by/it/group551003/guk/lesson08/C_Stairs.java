package by.it.group551003.guk.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Даны число 1<=n<=100 ступенек лестницы и
целые числа −10000<=a[1],…,a[n]<=10000, которыми помечены ступеньки.
Найдите максимальную сумму, которую можно получить, идя по лестнице
снизу вверх (от нулевой до n-й ступеньки), каждый раз поднимаясь на
одну или на две ступеньки.

Sample Input 1:
2
1 2
Sample Output 1:
3

Sample Input 2:
2
2 -1
Sample Output 2:
1

Sample Input 3:
3
-1 2 1
Sample Output 3:
3

*/

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n=scanner.nextInt();
        int stairs[]=new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i]=scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        if (n == 0) return 0;

        // dp[i] - макс сумма, чтобы добраться до i-й ступени (1..n)
        // dp[0] - стартовая позиция (пол), сумма 0
        int[] dp = new int[n + 1];

        dp[0] = 0;
        // Для первой ступеньки мы можем прийти только с пола (dp[0])
        dp[1] = dp[0] + stairs[0];

        for (int i = 2; i <= n; i++) {
            // Мы стоим на ступеньке i (это stairs[i-1])
            // Мы могли прийти сюда с i-1 или с i-2
            int prev1 = dp[i - 1];
            int prev2 = dp[i - 2];

            dp[i] = stairs[i - 1] + Math.max(prev1, prev2);
        }

        int result = dp[n];




        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
