package by.it.group510902.vishnevskaya.lesson08;

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

        // Создаем массив dp размером n+1, где dp[i] - максимальная сумма для достижения i-й ступеньки
        // (i от 0 до n, где 0 - это начало перед первой ступенькой)
        int[] dp = new int[n + 1];

        // Начальное значение - перед первой ступенькой сумма 0
        dp[0] = 0;

        // Для первой ступеньки (индекс 1 в dp соответствует stairs[0])
        if (n >= 1) {
            dp[1] = stairs[0];
        }

        // Для остальных ступенек
        for (int i = 2; i <= n; i++) {
            // На i-ю ступеньку можно попасть с (i-1)-й или с (i-2)-й
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i - 1];
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