package by.it.group551003.kalach.lesson08;

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

        // d[i] — максимальная сумма, чтобы дойти до ступеньки i
        int[] d = new int[n];

        // Базовый случай для первой ступеньки
        d[0] = stairs[0];

        if (n > 1) {
            // Для второй ступеньки: либо сразу на вторую, либо через первую
            // Но по условию мы идем с 0-й, значит на 2-ю (индекс 1)
            // можно попасть либо 0->1, либо 0->2(мимо)->... нет, прыжок только +1 или +2.
            // На индекс 1 можно попасть только: (0->0)+1 или (0->прыжок на 1)
            d[1] = Math.max(d[0] + stairs[1], stairs[1]);

            for (int i = 2; i < n; i++) {
                // Выбираем максимум между приходом с i-1 или i-2
                int step1 = d[i - 1];
                int step2 = d[i - 2];

                if (step1 > step2) {
                    d[i] = step1 + stairs[i];
                } else {
                    d[i] = step2 + stairs[i];
                }
            }
        }

        int result = d[n - 1];
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
