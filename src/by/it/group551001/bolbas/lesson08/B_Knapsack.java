package by.it.group551001.bolbas.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак без повторов

Первая строка входа содержит целые числа
    1<=W<=100000     вместимость рюкзака
    1<=n<=300        число золотых слитков
                    (каждый можно использовать только один раз).
Следующая строка содержит n целых чисел, задающих веса каждого из слитков:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

Найдите методами динамического программирования
максимальный вес золота, который можно унести в рюкзаке.

Sample Input:
10 3
1 4 8
Sample Output:
9

*/

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();  // количество слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаем массив для динамического программирования
        // dp[i] = максимальный вес, который можно набрать для вместимости i
        // Используем одномерный массив для оптимизации памяти
        boolean[] dp = new boolean[W + 1];
        dp[0] = true;  // вес 0 всегда можно набрать

        // Заполняем массив dp, обрабатывая каждый слиток
        // Идем от W вниз до gold[i], чтобы каждый слиток использовать не более одного раза
        for (int i = 0; i < n; i++) {
            for (int j = W; j >= gold[i]; j--) {
                if (dp[j - gold[i]]) {
                    dp[j] = true;
                }
            }
        }

        // Находим максимальный достижимый вес
        int result = 0;
        for (int i = W; i >= 0; i--) {
            if (dp[i]) {
                result = i;
                break;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
