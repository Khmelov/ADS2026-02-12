package by.it.a_khmelev.lesson07;

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
        int n = one.length();
        int m = two.length();
        int[][] dp = new int[n+1][m+1];
        char[][] back = new char[n+1][m+1]; // для хранения действий

// база
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            back[i][0] = '-';   // удаление
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            back[0][j] = '+';   // вставка
        }

// заполнение таблицы
        for (int i = 1; i <= n; i++) {
            char ci = one.charAt(i-1);
            for (int j = 1; j <= m; j++) {
                char cj = two.charAt(j-1);
                int cost = (ci == cj) ? 0 : 1;
                int del = dp[i-1][j] + 1;
                int ins = dp[i][j-1] + 1;
                int sub = dp[i-1][j-1] + cost;

                // выбор минимума с приоритетом sub -> del -> ins
                if (sub <= del && sub <= ins) {
                    dp[i][j] = sub;
                    back[i][j] = (cost == 0) ? '#' : '~';
                } else if (del <= ins) {
                    dp[i][j] = del;
                    back[i][j] = '-';
                } else {
                    dp[i][j] = ins;
                    back[i][j] = '+';
                }
            }
        }

// восстановление пути
        StringBuilder ops = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && (back[i][j] == '#' || back[i][j] == '~')) {
                if (back[i][j] == '#') {
                    ops.append("#,");
                } else { // '~'
                    ops.append("~").append(two.charAt(j-1)).append(",");
                }
                i--; j--;
            } else if (i > 0 && back[i][j] == '-') {
                ops.append("-").append(one.charAt(i-1)).append(",");
                i--;
            } else { // '+'
                ops.append("+").append(two.charAt(j-1)).append(",");
                j--;
            }
        }
        return ops.reverse().toString();


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