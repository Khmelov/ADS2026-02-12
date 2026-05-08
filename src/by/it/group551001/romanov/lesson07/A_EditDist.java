package by.it.group551001.romanov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
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


    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        int[][] memo = new int[n + 1][m + 1];

        // заполняем -1 (не вычислено)
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;
            }
        }

        return dist(one, two, n, m, memo);
    }

    int dist(String one, String two, int i, int j, int[][] memo) {
        if (memo[i][j] != -1) return memo[i][j];

        if (i == 0) return memo[i][j] = j;
        if (j == 0) return memo[i][j] = i;

        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = dist(one, two, i - 1, j - 1, memo);
        } else {
            int del = dist(one, two, i - 1, j, memo);
            int ins = dist(one, two, i, j - 1, memo);
            int rep = dist(one, two, i - 1, j - 1, memo);

            memo[i][j] = 1 + Math.min(del, Math.min(ins, rep));
        }

        return memo[i][j];
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
