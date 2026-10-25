package by.it.group551001.teryokhin.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {
    int n, m;
    int[][] dp;
    String one, two;

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        this.one = one;
        this.two = two;
        n = one.length();
        m = two.length();
        dp = new int[n + 1][m + 1];
        for(int[] line : dp)Arrays.fill(line, (int)1e9);
        dp[0][0] = 0;
        solve(n, m);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return dp[n][m];
    }

    int solve(int i, int j)
    {
        if(i == 0 && j == 0)return 0;
        if(dp[i][j] != (int)1e9)return dp[i][j];

        int new_value = (int)1e9;
        if(i != 0)
        {
            new_value = Math.min(new_value, solve(i - 1, j) + 1);
        }
        if(j != 0)
        {
            new_value = Math.min(new_value, solve(i, j - 1) + 1);
        }
        if(i != 0 && j != 0)
        {
            new_value = Math.min(new_value, solve(i - 1, j - 1) + (one.charAt(i - 1) != two.charAt(j - 1) ? 1 : 0));
        }

        return (dp[i][j] = new_value);
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
