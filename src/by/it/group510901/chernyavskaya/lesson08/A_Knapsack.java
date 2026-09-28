package by.it.group510901.chernyavskaya.lesson08;

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
*/

public class A_Knapsack {

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // W - вместимость рюкзака (максимальный вес)
        int n = scanner.nextInt();   // n - количество типов слитков
        int gold[] = new int[n];     // массив для хранения весов слитков
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt(); // заполняем массив весами слитков
        }

        // Создаем массив dp для динамического программирования
        // dp[i] будет хранить максимальный вес, который можно набрать
        // при вместимости рюкзака i
        int[] dp = new int[W + 1];

        // Инициализируем массив нулями (в Java это происходит автоматически)
        // dp[0] = 0 - при вместимости 0 можно набрать вес 0

        // Основной цикл: перебираем все возможные вместимости рюкзака от 1 до W
        for (int capacity = 1; capacity <= W; capacity++) {
            // Для текущей вместимости capacity перебираем все типы слитков
            for (int i = 0; i < n; i++) {
                // Если вес текущего слитка не превышает текущую вместимость
                if (gold[i] <= capacity) {
                    // Пробуем положить этот слиток в рюкзак
                    // dp[capacity - gold[i]] - максимальный вес для оставшегося места
                    // прибавляем gold[i] - вес текущего слитка
                    int candidate = dp[capacity - gold[i]] + gold[i];

                    // Если полученный вес больше текущего лучшего результата
                    // для вместимости capacity, обновляем dp[capacity]
                    if (candidate > dp[capacity]) {
                        dp[capacity] = candidate;
                    }
                }
            }
        }

        // Результат - максимальный вес, который можно набрать
        // при полной вместимости рюкзака W
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