package by.it.group510901.kirilenko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int m = one.length();
        int n = two.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {

                    dp[i][j] = 1 + Math.min(
                            Math.min(dp[i - 1][j],      // удаление
                                    dp[i][j - 1]),     // вставка
                            dp[i - 1][j - 1]            // замена
                    );
                }
            }
        }


        List<String> ops = new ArrayList<>();
        int i = m, j = n;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                ops.add("#");  // копирование
                i--;
                j--;
            }

            else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                ops.add("+" + two.charAt(j - 1));  // вставка
                j--;
            }
            // Приоритет 3: замена
            else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                ops.add("~" + two.charAt(j - 1));  // замена
                i--;
                j--;
            }
            // Приоритет 4: удаление
            else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                ops.add("-" + one.charAt(i - 1));  // удаление
                i--;
            }
        }

        Collections.reverse(ops);


        StringBuilder result = new StringBuilder();
        for (String op : ops) {
            result.append(op).append(",");
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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