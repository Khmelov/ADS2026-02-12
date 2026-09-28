package by.it.group551003.matskevich.lesson07;

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
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int m = one.length();
        int n = two.length();

        // Создаем таблицу для динамического программирования
        // dp[i][j] - расстояние между первыми i символами one и первыми j символами two
        int[][] dp = new int[m + 1][n + 1];

        // Базовые случаи:
        // Если одна строка пустая, расстояние равно длине второй строки
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // удаление всех символов из one
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // вставка всех символов из two
        }

        // Заполняем таблицу
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Символы совпадают - берем значение из диагонали без изменений
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Символы не совпадают - берем минимум из трех операций + 1
                    dp[i][j] = 1 + Math.min(
                            Math.min(
                                    dp[i - 1][j],     // удаление символа из one
                                    dp[i][j - 1]      // вставка символа из two
                            ),
                            dp[i - 1][j - 1]      // замена символа
                    );
                }
            }
        }

        int result = dp[m][n];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
