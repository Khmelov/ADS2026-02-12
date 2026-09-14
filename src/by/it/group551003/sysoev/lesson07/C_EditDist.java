package by.it.group551003.sysoev.lesson07;

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

        // создаём таблицу для хранения операций
        int[][] dp = new int[n + 1][m + 1];
        char[][] op = new char[n + 1][m + 1];

        // инициализация
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            if (i > 0) {
                op[i][0] = '-';
            }
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            if (j > 0) {
                op[0][j] = '+';
            }
        }

        // заполняем таблицу
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    op[i][j] = '#';
                } else {
                    // считаем три варианта
                    int replace = dp[i - 1][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;
                    int insert = dp[i][j - 1] + 1;

                    // выбираем минимальный
                    if (replace <= delete && replace <= insert) {
                        dp[i][j] = replace;
                        op[i][j] = '~';
                    } else if (delete <= insert && delete <= replace) {
                        dp[i][j] = delete;
                        op[i][j] = '-';
                    } else {
                        dp[i][j] = insert;
                        op[i][j] = '+';
                    }
                }
            }
        }

        // восстанавливаем редакционное предписание
        StringBuilder resultBuilder = new StringBuilder();
        int i = n;
        int j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && op[i][j] == '#') {
                resultBuilder.insert(0, "#,");
                i--;
                j--;
            } else if (i > 0 && j > 0 && op[i][j] == '~') {
                resultBuilder.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            } else if (i > 0 && op[i][j] == '-') {
                resultBuilder.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            } else if (j > 0 && op[i][j] == '+') {
                resultBuilder.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }

        String result = resultBuilder.toString();

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