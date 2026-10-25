package by.it.group551003.yakushev.lesson07;

import java.io.FileInputStream;
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
        int[][] dp = new int[one.length() + 1][two.length() + 1];
        for (int i = 0; i < two.length(); ++i) {
            dp[0][i] = i;
        }
        for (int i = 0; i < one.length(); ++i) {
            dp[i][0] = i;
        }

        for (int i = 1; i <= one.length(); ++i) {
            for (int j = 1; j <= two.length(); ++j) {
                dp[i][j] = Math.min(
                        dp[i][j - 1] + 1,
                        Math.min(
                                dp[i - 1][j] + 1,
                                dp[i - 1][j - 1] + (one.charAt(i - 1) != two.charAt(j - 1) ? 1 : 0)
                        )
                );
            }
        }

        StringBuilder result = new StringBuilder();

        int i = one.length();
        int j = two.length();

        while (i > 0 && j > 0) {
            int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

            if (dp[i][j] == dp[i - 1][j - 1] + cost) {
                result.append(one.charAt(i - 1) == two.charAt(j - 1) ? "#," : "~" + one.charAt(i - 1) + ",");
                i--; j--;
            } else if (dp[i][j] == dp[i - 1][j] + 1) {
                result.append("-").append(one.charAt(i - 1)).append(",");
                i--;
            } else {
                result.append("+").append(two.charAt(j - 1)).append(",");
                j--;
            }
        }

        while (i > 0) {
            result.append("-,");
            i--;
        }
        while (j > 0) {
            result.append("+,");
            j--;
        }

        System.out.println(result);
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