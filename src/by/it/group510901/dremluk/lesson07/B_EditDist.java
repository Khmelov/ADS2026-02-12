package by.it.group510901.dremluk.lesson07;

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
    /*даны две строки и нужно посчитать количество операций, чтобы они стали одинаковые. у нас создается */
    int getDistanceEdinting(String one, String two) {
        /*таблица зависимости от индекса и с помощью формулы перехода мы определяем какая операция будет использоваться*/
        int m = one.length();   // длина первой строки
        int n = two.length();   // длина второй строки

        // создаём DP-таблицу (m+1) x (n+1)
        int[][] dp = new int[m + 1][n + 1];

        // заполняем первый столбец — удаляем символы из one
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        // заполняем первую строку — вставляем символы в one
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // заполняем таблицу динамического программирования
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {

                // если буквы совпадают — операция не нужна
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // три варианта: вставка, удаление, замена
                    int insert = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;
                    int replace = dp[i - 1][j - 1] + 1;

                    // выбираем минимальную стоимость
                    dp[i][j] = Math.min(Math.min(insert, delete), replace);
                }
            }
        }

        // итоговое расстояние Левенштейна
        return dp[m][n];
    }


    public static void main(String[] args) throws FileNotFoundException {

        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);

        // читаем пары строк и выводим расстояние
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}