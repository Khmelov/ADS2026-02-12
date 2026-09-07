package by.it.group510901.filimonova.lesson01.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: рюкзак с повторами

Первая строка входа содержит целые числа
    1<=W<=100000     вместимость рюкзака
    1<=n<=300        сколько есть вариантов золотых слитков
                     (каждый можно использовать множество раз).
Следующая строка содержит n целых чисел, задающих веса слитков:
  0<=w[1]<=100000 ,..., 0<=w[n]<=100000

Найдите методами динамического программирования
максимальный вес золота, который можно унести в рюкзаке.


Sample Input:
10 3
1 4 8
Sample Output:
10

Sample Input 2:

15 3
2 8 16
Sample Output 2:
14

*/

public class A_Knapsack {

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();  // количество типов слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаем массив dp, где dp[i] - максимальный вес, который можно набрать
        // для вместимости i (рюкзака весом i)
        int[] dp = new int[W + 1];

        // Инициализация: dp[0] = 0 (для рюкзака вместимостью 0 максимальный вес 0)
        dp[0] = 0;

        // Заполняем массив dp для всех вместимостей от 1 до W
        for (int i = 1; i <= W; i++) {
            // Для каждого веса i пытаемся использовать все типы слитков
            for (int j = 0; j < n; j++) {
                if (gold[j] <= i) {
                    // Если слиток помещается, обновляем максимальный вес
                    dp[i] = Math.max(dp[i], dp[i - gold[j]] + gold[j]);
                }
            }
        }

        int result = dp[W];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
/*dp[i] — максимальный вес для вместимости i

dp[0] = 0

Для каждой вместимости i от 1 до W:

Перебираем все слитки

Если слиток веса gold[j] помещается (gold[j] <= i)

dp[i] = max(dp[i], dp[i - gold[j]] + gold[j])

Особенность: проход от 1 до W (прямой порядок) позволяет использовать слитки многократно

Сложность: O(W × n)*/