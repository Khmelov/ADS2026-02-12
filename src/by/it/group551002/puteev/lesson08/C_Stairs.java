package by.it.group551002.puteev.lesson08;

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
        int result = 0;
        // Если ступенек нет, сумма 0
        if (n == 0) return 0;

        // Массив для хранения максимальной суммы на каждой ступеньке
        int[] dp = new int[n];

        // Базовые случаи:
        // Первая ступенька — на неё можно только наступить
        dp[0] = stairs[0];

        if (n > 1) {
            // Вторая ступенька — можно прийти сразу на неё (пропустив первую)
            // или наступить на первую, а потом на вторую.
            // Но по условию мы ИДЕМ по лестнице, начиная с 0-й (земли).
            // Значит на 2-ю ступеньку можно попасть либо (0->2), либо (0->1->2)
            dp[1] = Math.max(stairs[1], dp[0] + stairs[1]);
        }

        // Заполняем остальные ступеньки
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i];
        }

        result = dp[n - 1];



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
