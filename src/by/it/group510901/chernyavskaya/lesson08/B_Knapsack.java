package by.it.group510901.chernyavskaya.lesson08;

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
*/

public class B_Knapsack {

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();     // W - вместимость рюкзака
        int n = scanner.nextInt();     // n - количество слитков
        int gold[] = new int[n];       // массив для хранения весов слитков
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt(); // заполняем массив весами слитков
        }

        // Создаем двумерный массив dp
        int[][] dp = new int[n + 1][W + 1];

        // перебираем все слитки
        for (int i = 1; i <= n; i++) {
            // Текущий слиток (индекс i-1, так как массив gold начинается с 0)
            int currentGold = gold[i - 1];

            // Перебираем все возможные вместимости рюкзака от 1 до W
            for (int capacity = 1; capacity <= W; capacity++) {
                // Не берем текущий слиток
                // Тогда результат такой же, как для первых (i-1) слитков
                dp[i][capacity] = dp[i - 1][capacity];

                //  Берем текущий слиток (если он помещается)
                if (currentGold <= capacity) {
                    // Вес если возьмем слиток
                    int takeGold = dp[i - 1][capacity - currentGold] + currentGold;
                    // Выбираем максимальный вариант: брать или не брать
                    if (takeGold > dp[i][capacity]) {
                        dp[i][capacity] = takeGold;
                    }
                }
            }
        }

        // Результат: максимальный вес

        int result = dp[n][W];

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