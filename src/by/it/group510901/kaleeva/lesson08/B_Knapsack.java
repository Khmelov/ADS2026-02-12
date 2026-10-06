package by.it.group510901.kaleeva.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача: рюкзак без повторов (0/1 рюкзак)
- Каждый слиток можно использовать ТОЛЬКО ОДИН раз
- Нужно набрать максимальный вес, не превышающий вместимость W
*/

public class B_Knapsack {

    int getMaxWeight(InputStream stream) {
        // Создаём сканер для чтения входных данных
        Scanner scanner = new Scanner(stream);

        // W - вместимость рюкзака
        int W = scanner.nextInt();

        // n - количество слитков
        int n = scanner.nextInt();

        // Массив с весами слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаём двумерный массив dp для динамического программирования
        // dp[i][w] - максимальный вес, который можно набрать,
        // используя первые i слитков, при вместимости w
        // Оптимизация: используем только две строки (текущую и предыдущую)
        int[] dp = new int[W + 1];

        // Основной алгоритм для рюкзака БЕЗ ПОВТОРОВ (0/1 рюкзак)
        // Перебираем все слитки
        for (int i = 0; i < n; i++) {
            // ВАЖНО: идём ОТ БОЛЬШЕГО веса К МЕНЬШЕМУ
            // Чтобы не использовать один слиток несколько раз
            for (int w = W; w >= gold[i]; w--) {
                // Решаем: брать или не брать текущий слиток
                // dp[w] - не берём слиток
                // dp[w - gold[i]] + gold[i] - берём слиток
                dp[w] = Math.max(dp[w], dp[w - gold[i]] + gold[i]);
            }
        }

        // Ответ - максимальный вес при вместимости W
        return dp[W];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}