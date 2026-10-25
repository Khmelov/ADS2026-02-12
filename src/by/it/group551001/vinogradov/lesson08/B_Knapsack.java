package by.it.group551001.vinogradov.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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
        int[] gold =new int[n];
        ArrayList<Integer>[] dp = new ArrayList[w+1];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }
        for (int i = 0; i <= w; i++) {
            dp[i] = new ArrayList<>();
            dp[i].add(0);
        }
        for (int i = 0; i <= w; i++) {
            if (i > 0) dp[i].set(0, dp[i-1].getFirst());
            int curr = -1;
            for (int j = 0; j < n; j++) {
                if (gold[j] > i) continue;
                int target = i - gold[j];
                boolean f = false;
                for (int k = 1; k < dp[target].size(); k++) {
                    if (dp[target].get(k) == j) {
                        f = true;
                        break;
                    }
                }
                if (!f && gold[j] + dp[target].getFirst() > dp[i].getFirst()) {
                    dp[i] = new ArrayList<>(dp[target]);
                    dp[i].set(0, dp[i].getFirst() + gold[j]);
                    curr = j;
                }
            }
            if (curr > -1)
                dp[i].add(curr);
        }
        return dp[w].getFirst();
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
