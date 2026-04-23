package by.it.group551001.biarezina.lesson08;

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
        // Если ступенек нет, сумма 0
        if (n == 0) return 0;

        // Создаем массив для хранения максимальных сумм на каждой ступеньке
        int[] d = new int[n];

        // Базовые случаи:
        // Первая ступенька — ее значение
        d[0] = stairs[0];

        if (n > 1) {
            // На вторую можно прийти либо сразу (stairs[0]+stairs[1]),
            // либо прыгнуть через одну (но в данной задаче мы стартуем с "земли",
            // поэтому на вторую можно попасть только наступив на нее).
            // Алгоритм выбора: d[1] = max(наступить на 1-ю и 2-ю, или прыгнуть сразу на 2-ю)
            d[1] = Math.max(stairs[0] + stairs[1], stairs[1]);

            // Основной цикл для всех последующих ступенек
            for (int i = 2; i < n; i++) {
                // Сумма на i-й ступеньке = значение ступеньки + макс(предыдущая, пред-предыдущая)
                d[i] = Math.max(d[i - 1], d[i - 2]) + stairs[i];
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
