package by.it.group551001.myadelets.lesson07;

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
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                int del = dp[i - 1][j] + 1;
                int ins = dp[i][j - 1] + 1;
                int sub = dp[i - 1][j - 1] + cost;

                dp[i][j] = Math.min(del, Math.min(ins, sub));
            }
        }

        StringBuilder rev = new StringBuilder();
        int i = n;
        int j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0) {
                char c1 = one.charAt(i - 1);
                char c2 = two.charAt(j - 1);

                if (c1 == c2 && dp[i][j] == dp[i - 1][j - 1]) {
                    rev.append('#').append(',');
                    i--;
                    j--;
                } else if (dp[i][j] == dp[i - 1][j - 1] + 1) {
                    rev.append('~').append(',');
                    i--;
                    j--;
                } else if (dp[i][j] == dp[i - 1][j] + 1) {
                    rev.append('-').append(',');
                    i--;
                } else {
                    rev.append('+').append(',');
                    j--;
                }
            } else if (i > 0) {
                rev.append('-').append(',');
                i--;
            } else {
                rev.append('+').append(',');
                j--;
            }
        }

        StringBuilder result = new StringBuilder();
        String[] ops = rev.toString().split(",");
        for (int k = ops.length - 1; k >= 0; k--) {
            if (ops[k].isEmpty()) continue;
            result.append(ops[k]).append(',');
        }

        return result.toString();
    }
}