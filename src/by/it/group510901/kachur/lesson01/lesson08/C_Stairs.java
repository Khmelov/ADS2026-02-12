package by.it.group510901.kachur.lesson01.lesson08;

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
        // dp[i] - максимальная сумма для достижения ступеньки i
        int[] dp = new int[n + 1];

        // Базовый случай: на старте сумма = 0
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            // Максимум из двух вариантов:
            // - пришли с предыдущей ступеньки (i-1)
            // - пришли с предпредыдущей (i-2), если она существует
            int fromPrev = dp[i - 1];
            int fromPrevPrev = (i >= 2) ? dp[i - 2] : Integer.MIN_VALUE;

            // Прибавляем ценность текущей ступеньки (индекс i-1 в массиве stairs)
            dp[i] = Math.max(fromPrev, fromPrevPrev) + stairs[i - 1];
        }

        return dp[n];
    }



    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res=instance.getMaxSum(stream);
        System.out.println(res);
    }

}
