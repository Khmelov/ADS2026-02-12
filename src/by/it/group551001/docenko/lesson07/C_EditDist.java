package by.it.group551001.docenko.lesson07;

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
        int[][] dp = new int[one.length()+1][two.length()+1];
        dp[0][0] = 0;

        for (int row = 1; row <= one.length(); row++)
            dp[row][0] = row;

        for (int col = 1; col <= two.length(); col++)
            dp[0][col] = col;

        for (int row = 1; row <= one.length(); row++) {
            for (int col = 1; col <= two.length(); col++) {
                int cost = (one.charAt(row-1) == two.charAt(col-1)) ? 0 : 1;
                dp[row][col] = Math.min(Math.min(dp[row-1][col]+1, dp[row][col-1]+1), dp[row-1][col-1]+cost);
            }
        }

        StringBuilder result = new StringBuilder();
        int row = one.length(), col = two.length();

        while (row > 0 || col > 0) {
            if (row > 0 && col > 0 && one.charAt(row-1) == two.charAt(col-1)) {
                result.insert(0, "#,");
                row--;
                col--;
            }
            else if (row > 0 && col > 0 && dp[row][col] == dp[row-1][col-1] + 1) {
                result.insert(0, "~" + two.charAt(col-1) + ",");
                row--;
                col--;
            }
            else if (row > 0 && dp[row][col] == dp[row-1][col] + 1) {
                result.insert(0, "-" + one.charAt(row-1) + ",");
                row--;
            }
            else {
                result.insert(0, "+" + two.charAt(col-1) + ",");
                col--;
            }
        }
        return result.toString();
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