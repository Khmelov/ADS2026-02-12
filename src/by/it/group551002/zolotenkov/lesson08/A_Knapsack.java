package by.it.group551002.zolotenkov.lesson08;

import java.io.FileInputStream;
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
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }
        int[] dp = new int[w + 1];
        for (int j = 1; j <= w; j++) {
            dp[j] = -1; // недостижимый вес
        }
        dp[0] = 0;
        int result = 0;

        // Заполняем динамику: для каждой вместимости пытаемся добавить слитки
        for (int j = 0; j <= w; j++) {
            if (dp[j] >= 0) {
                if (dp[j] > result) {
                    result = dp[j]; // обновляем максимум
                }
                for (int i = 0; i < n; i++) {
                    int next = j + gold[i];
                    if (next <= w && dp[j] + gold[i] > dp[next]) {
                        dp[next] = dp[j] + gold[i];
                    }
                }
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
