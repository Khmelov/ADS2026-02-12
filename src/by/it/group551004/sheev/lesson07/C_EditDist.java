package by.it.group551004.sheev.lesson07;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();

        //Мин расст ред
        int[][] dp = new int[n + 1][m + 1];

        //Хранен опер
        String[][] op = new String[n + 1][m + 1];

        //Иниц баз случ
        for (int i = 1; i <= n; i++) {
            dp[i][0] = i;
            op[i][0] = "-" + one.charAt(i - 1);
        }
        for (int j = 1; j <= m; j++) {
            dp[0][j] = j;
            op[0][j] = "+" + two.charAt(j - 1);
        }
        //Заполн
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char c1 = one.charAt(i - 1);
                char c2 = two.charAt(j - 1);

                if (c1 == c2) {
                    dp[i][j] = dp[i - 1][j - 1];
                    op[i][j] = "#";
                } else {
                    int del = dp[i - 1][j] + 1;
                    int ins = dp[i][j - 1] + 1;
                    int rep = dp[i - 1][j - 1] + 1;

                    if (del <= ins && del <= rep) {
                        dp[i][j] = del;
                        op[i][j] = "-" + c1;
                    } else if (ins <= del && ins <= rep) {
                        dp[i][j] = ins;
                        op[i][j] = "+" + c2;
                    } else {
                        dp[i][j] = rep;
                        op[i][j] = "~" + c2;
                    }
                }
            }
        }

        //Восст посл
        StringBuilder result = new StringBuilder();
        int i = n, j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && op[i][j] != null && op[i][j].equals("#")) {
                result.insert(0, "#,");
                i--;
                j--;
            }
            else if (i > 0 && j > 0 && op[i][j] != null && op[i][j].charAt(0) == '~') {
                result.insert(0, "~" + op[i][j].charAt(1) + ",");
                i--;
                j--;
            }
            else if (i > 0 && op[i][j] != null && op[i][j].charAt(0) == '-') {
                result.insert(0, "-" + op[i][j].charAt(1) + ",");
                i--;
            }
            else if (j > 0 && op[i][j] != null && op[i][j].charAt(0) == '+') {
                result.insert(0, "+" + op[i][j].charAt(1) + ",");
                j--;
            }
            else if (i > 0 && j == 0) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
            else if (j > 0 && i == 0) {
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }

        return result.toString();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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