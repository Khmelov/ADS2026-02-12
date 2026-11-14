package by.it.group551001.maxim_belyak.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();  // число золотых слитков
        int gold[] = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаем DP массив: dp[w] - максимальный вес, который можно набрать
        // с вместимостью w используя рассмотренные слитки
        boolean[] dp = new boolean[W + 1];
        dp[0] = true;  // всегда можно набрать 0 вес

        // Для каждого слитка
        for (int i = 0; i < n; i++) {
            // Идем от большей вместимости к меньшей, чтобы каждый слиток использовать только один раз
            for (int w = W; w >= gold[i]; w--) {
                if (dp[w - gold[i]]) {
                    dp[w] = true;
                }
            }
        }

        // Находим максимальный достижимый вес
        int result = 0;
        for (int w = W; w >= 0; w--) {
            if (dp[w]) {
                result = w;
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