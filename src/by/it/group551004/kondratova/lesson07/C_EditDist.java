package by.it.group551004.kondratova.lesson07;

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

*/

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        // Таблица для хранения операций
        int[][] dp = new int[m + 1][n + 1];
        char[][] op = new char[m + 1][n + 1]; // 'D' - delete, 'I' - insert, 'R' - replace, 'M' - match

        // Инициализация
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            op[i][0] = 'D'; // удаление
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
            op[0][j] = 'I'; // вставка
        }
        op[0][0] = ' ';

        // Заполнение таблицы
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    op[i][j] = 'M'; // совпадение
                } else {
                    int delete = dp[i - 1][j] + 1;
                    int insert = dp[i][j - 1] + 1;
                    int replace = dp[i - 1][j - 1] + 1;

                    if (delete <= insert && delete <= replace) {
                        dp[i][j] = delete;
                        op[i][j] = 'D'; // удаление
                    } else if (insert <= delete && insert <= replace) {
                        dp[i][j] = insert;
                        op[i][j] = 'I'; // вставка
                    } else {
                        dp[i][j] = replace;
                        op[i][j] = 'R'; // замена
                    }
                }
            }
        }

        // Восстановление редакционного предписания
        StringBuilder result = new StringBuilder();
        int i = m;
        int j = n;

        while (i > 0 || j > 0) {
            char operation = op[i][j];
            switch (operation) {
                case 'M': // совпадение
                    result.insert(0, "#,");
                    i--;
                    j--;
                    break;
                case 'D': // удаление
                    result.insert(0, "-" + one.charAt(i - 1) + ",");
                    i--;
                    break;
                case 'I': // вставка
                    result.insert(0, "+" + two.charAt(j - 1) + ",");
                    j--;
                    break;
                case 'R': // замена
                    result.insert(0, "~" + two.charAt(j - 1) + ",");
                    i--;
                    j--;
                    break;
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}