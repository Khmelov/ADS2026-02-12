package by.it.group510901.filimonova.lesson01.lesson08;

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
        int W = scanner.nextInt();  // вместимость рюкзака
        int n = scanner.nextInt();  // количество слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // Создаем массив dp, где dp[i] - максимальный вес, который можно набрать
        // для вместимости i (рюкзака весом i)
        int[] dp = new int[W + 1];

        // Для каждого слитка обновляем массив dp в обратном порядке
        // (чтобы каждый слиток использовался не более одного раза)
        for (int i = 0; i < n; i++) {
            // Идем от W вниз до веса текущего слитка
            for (int j = W; j >= gold[i]; j--) {
                // Решаем: брать слиток или не брать
                dp[j] = Math.max(dp[j], dp[j - gold[i]] + gold[i]);
            }
        }

        int result = dp[W];
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
/*dp[j] — максимальный вес для вместимости j

Для каждого слитка обходим массив от W вниз

dp[j] = max(dp[j], dp[j - gold[i]] + gold[i])

Почему обратный порядок?

При обходе сверху вниз (от W до 0) каждый слиток учитывается только один раз

Если идти снизу вверх, слиток может быть использован многократно

Сложность: O(W × n)*/