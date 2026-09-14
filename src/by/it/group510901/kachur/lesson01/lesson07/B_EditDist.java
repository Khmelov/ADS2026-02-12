package by.it.group510901.kachur.lesson01.lesson07;

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
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();

        // Создаём таблицу (n+1) x (m+1)
        int[][] dp = new int[n + 1][m + 1];




        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }


        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        // ===== ШАГ 2: Заполняем таблицу =====

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Сравниваем текущие символы (индексы i-1 и j-1)
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Символы совпадают → просто копируем значение из диагонали
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Символы разные → берём минимум из трёх операций
                    int delete = dp[i - 1][j] + 1;   // удаление из one
                    int insert = dp[i][j - 1] + 1;   // вставка в one (удаление из two)
                    int replace = dp[i - 1][j - 1] + 1; // замена

                    dp[i][j] = Math.min(delete, Math.min(insert, replace));
                }
            }
        }


        return dp[n][m];

    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}