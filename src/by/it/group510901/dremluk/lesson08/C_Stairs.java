package by.it.group510901.dremluk.lesson08;

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
/*дана лестница с n ступеньками, каждая ступенька имеет свой вес (может быть отрицательным). */
        Scanner scanner = new Scanner(stream);
/*ужно подняться с нулевой ступеньки (земли) до n-ой ступеньки, двигаясь только вверх на 1 или 2 ступеньки за раз,*/
        int n = scanner.nextInt();      // количество ступенек
        int[] stairs = new int[n];      // значения ступенек
/* при этом можно пропускать ступеньки (перешагивать через одну), но обязательно наступаем на последнюю n-ю ступеньку*/
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }
/*нужно набрать максимальную сумму весов ступенек, на которые наступаем.*/
        // dp[i] — максимальная сумма, чтобы попасть на i‑ю ступеньку
        // dp[0] — старт перед лестницей (сумма = 0)
        int[] dp = new int[n + 1];

        dp[0] = 0;

        // первая ступенька (индекс 1 в dp соответствует stairs[0])
        if (n >= 1) {
            dp[1] = stairs[0];
        }

        // считаем для всех остальных ступенек
        for (int i = 2; i <= n; i++) {

            // можно прийти с i‑1 или i‑2 ступеньки
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i - 1];
        }

        return dp[n]; // максимальная сумма на последней ступеньке
    }


    public static void main(String[] args) throws FileNotFoundException {

        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();

        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }

}