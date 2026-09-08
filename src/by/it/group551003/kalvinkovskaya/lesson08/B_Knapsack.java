package by.it.group551003.kalvinkovskaya.lesson08;

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
        int max_weight=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n + 1];
        for (int i = 1; i <= n; i++) {
            gold[i]=scanner.nextInt();
        }
        int result = 0;

        int[][] d = new int[max_weight + 1][n + 1];
        for (int w = 0; w <= max_weight; w++)
            d[w][0] = 0;
        for (int i = 0; i <= n; i++)
            d[0][i] = 0;
        for (int i = 1; i <= n; i++)
            for (int w = 1; w <= max_weight; w++) {
                d[w][i] = d[w][i - 1];
                if (gold[i] <= w) {
                    int val = d[w - gold[i]][i - 1] + gold[i];
                    if (d[w][i] < val)
                        d[w][i] = val;
                }
            }
        result = d[max_weight][n];
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
