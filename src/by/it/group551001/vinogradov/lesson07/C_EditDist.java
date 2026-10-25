package by.it.group551001.vinogradov.lesson07;

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
        int n = one.length(), m = two.length();
        int[][] dp = new int[n + 1][m + 1];
        char[][] choice = new char[n + 1][m + 1];

        // Initialize borders
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            choice[i][0] = 'D';
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            choice[0][j] = 'I';
        }
        choice[0][0] = 'S';

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                int del = dp[i - 1][j] + 1;
                int ins = dp[i][j - 1] + 1;
                int rep = dp[i - 1][j - 1] + cost;
                if (del <= ins && del <= rep) {
                    dp[i][j] = del;
                    choice[i][j] = 'D';
                } else if (ins <= del && ins <= rep) {
                    dp[i][j] = ins;
                    choice[i][j] = 'I';
                } else {
                    dp[i][j] = rep;
                    choice[i][j] = (cost == 0) ? 'M' : 'R';
                }
            }
        }
        StringBuilder rev = new StringBuilder();
        rev.append(",");
        int i = n, j = m;
        while (i > 0 || j > 0) {
            char op = choice[i][j];
            switch (op) {
                case 'D':
                    rev.append(",").append(one.charAt(i - 1)).append("-");
                    i--;
                    break;
                case 'I':
                    rev.append(",").append(two.charAt(j - 1)).append("+");
                    j--;
                    break;
                case 'R':
                    rev.append(",").append(two.charAt(j - 1)).append("~");
                    i--;
                    j--;
                    break;
                case 'M':
                    rev.append(",#");
                    i--;
                    j--;
                    break;
            }
        }
        String result = rev.reverse().toString();
        return result.substring(0, result.length()-1);
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}