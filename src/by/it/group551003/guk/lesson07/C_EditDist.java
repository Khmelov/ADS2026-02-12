package by.it.group551003.guk.lesson07;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

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
                int delete = dp[i - 1][j] + 1;
                int insert = dp[i][j - 1] + 1;
                int replace = dp[i - 1][j - 1] + cost;
                dp[i][j] = Math.min(Math.min(delete, insert), replace);
            }
        }

        List<String> operations = new ArrayList<>();
        int i = n;
        int j = m;

        while (i > 0 || j > 0) {
            int currentCost = dp[i][j];
            boolean moved = false;

            if (i > 0 && j > 0) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                if (currentCost == dp[i - 1][j - 1] + cost) {
                    if (cost == 0) {
                        operations.add("#");
                    } else {
                        operations.add("~" + two.charAt(j - 1));
                    }
                    i--;
                    j--;
                    moved = true;
                }
            }

            if (!moved && i > 0 && currentCost == dp[i - 1][j] + 1) {
                operations.add("-" + one.charAt(i - 1));
                i--;
                moved = true;
            }

            if (!moved && j > 0 && currentCost == dp[i][j - 1] + 1) {
                operations.add("+" + two.charAt(j - 1));
                j--;
                moved = true;
            }

            if (!moved) break;
        }

        Collections.reverse(operations);

        StringBuilder sb = new StringBuilder();
        for (String op : operations) {
            sb.append(op).append(",");
        }

        String result = sb.toString();




        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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