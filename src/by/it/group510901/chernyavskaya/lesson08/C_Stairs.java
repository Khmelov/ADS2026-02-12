package by.it.group510901.chernyavskaya.lesson08;

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
        int n = scanner.nextInt();        // n - количество ступенек
        int stairs[] = new int[n];        // массив для хранения значений на ступеньках
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt(); // заполняем массив значениями ступенек
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Создаем массив dp для динамического программирования
        // dp[i] - максимальная сумма, которую можно получить,
        // чтобы добраться до ступеньки i (индексация с 0)
        int[] dp = new int[n];

        // Обрабатываем случай с одной ступенькой
        if (n >= 1) {
            dp[0] = stairs[0];  // до первой ступеньки можно добраться только так
        }

        // Обрабатываем случай с двумя ступеньками
        if (n >= 2) {
            // До второй ступеньки можно добраться:
            // - с нулевой ступеньки (шаг в 2 ступеньки)
            // ИЛИ
            // - с первой ступеньки (шаг в 1 ступеньку)
            // Выбираем максимальную сумму
            dp[1] = Math.max(stairs[0] + stairs[1], stairs[1]);
        }

        // Основной цикл: для каждой ступеньки от 2 до n-1
        for (int i = 2; i < n; i++) {
            // На ступеньку i можно попасть:
            // 1. Ступенька i-1 (шаг на 1 ступеньку) -> тогда нужно прибавить stairs[i]
            // 2. Ступенька i-2 (шаг на 2 ступеньки) -> тогда нужно прибавить stairs[i]
            // Выбираем максимальный путь из этих двух вариантов
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i];
        }

        // Результат: максимальная сумма для достижения последней ступеньки
        // Если ступенек нет (n=0), то результат 0
        int result = (n > 0) ? dp[n - 1] : 0;

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