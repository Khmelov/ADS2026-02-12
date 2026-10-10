package by.it.group551003.kardash.lesson08;

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

        // Базовые случаи
        if (n == 0) {
            return 0;
        }

        // Создаем DP массив для хранения максимальной суммы до каждой ступеньки
        // dp[i] - максимальная сумма, которую можно получить, дойдя до i-й ступеньки
        int[] dp = new int[n];

        // Для первой ступеньки (индекс 0) максимальная сумма равна ее значению
        dp[0] = stairs[0];

        // Для второй ступеньки (индекс 1) максимальная сумма - максимум из:
        // - подняться на одну ступеньку (0 -> 1)
        // - подняться на две ступеньки (сразу с земли на ступеньку 1, если n>1)
        if (n > 1) {
            dp[1] = stairs[1] + Math.max(0, dp[0]);
        }

        // Заполняем DP массив для остальных ступенек
        for (int i = 2; i < n; i++) {
            // На i-ю ступеньку можно попасть:
            // 1) с предыдущей ступеньки (i-1) - подняться на одну ступеньку
            // 2) с позапрошлой ступеньки (i-2) - подняться на две ступеньки
            // Выбираем максимальный путь и добавляем значение текущей ступеньки
            dp[i] = stairs[i] + Math.max(dp[i-1], dp[i-2]);
        }

        // Результат - максимальная сумма на последней ступеньке
        int result = dp[n-1];

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
