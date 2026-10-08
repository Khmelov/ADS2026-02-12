package by.it.group551003.kardash.lesson08;

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

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }

        // Создаем DP массив: dp[weight] = максимальный вес для вместимости weight
        // Используем 1D массив для оптимизации памяти
        int[] dp = new int[w + 1];

        // Инициализируем dp[0] = 0 (пустой рюкзак)
        dp[0] = 0;

        // Для каждого слитка
        for (int i = 0; i < n; i++) {
            // Идем от большей вместимости к меньшей, чтобы каждый слиток использовать только один раз
            for (int capacity = w; capacity >= gold[i]; capacity--) {
                // Пробуем положить текущий слиток или не класть его
                // dp[capacity] - не кладем слиток
                // dp[capacity - gold[i]] + gold[i] - кладем слиток
                dp[capacity] = Math.max(dp[capacity], dp[capacity - gold[i]] + gold[i]);
            }
        }

        int result = dp[w];  // Максимальный вес для полной вместимости
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}