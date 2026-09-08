package by.it.group510901.kaleeva.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача: рюкзак с повторами (неограниченный рюкзак)
- Можно использовать каждый слиток неограниченное количество раз
- Нужно набрать максимальный вес, не превышающий вместимость W
*/

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        // Создаём сканер для чтения входных данных
        Scanner scanner = new Scanner(stream);

        // W - вместимость рюкзака (максимальный вес, который можно унести)
        int W = scanner.nextInt();

        // n - количество различных типов слитков
        int n = scanner.nextInt();

        // Массив с весами слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаём массив dp для динамического программирования
        // dp[i] - максимальный вес, который можно набрать при вместимости i
        // Размер W+1, чтобы индексы шли от 0 до W
        boolean[] dp = new boolean[W + 1];

        // Базовый случай: при вместимости 0 можно набрать вес 0
        dp[0] = true;

        // Идём по всем возможным весам от 0 до W
        for (int i = 0; i <= W; i++) {
            // Если текущий вес i достижим
            if (dp[i]) {
                // Пробуем добавить каждый слиток
                for (int j = 0; j < n; j++) {
                    // Если после добавления слитка не превышаем вместимость
                    if (i + gold[j] <= W) {
                        // Отмечаем новый вес как достижимый
                        dp[i + gold[j]] = true;
                    }
                }
            }
        }

        // Ищем максимальный достижимый вес (идём с конца)
        int result = 0;
        for (int i = W; i >= 0; i--) {
            if (dp[i]) {
                result = i;
                break;
            }
        }

        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}