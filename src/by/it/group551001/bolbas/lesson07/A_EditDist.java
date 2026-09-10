package by.it.group551001.bolbas.lesson07;

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

        // Создаем таблицу для мемоизации, инициализируем -1 (не вычислено)
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = -1;
            }
        }

        // Рекурсивная функция
        result = editDistRecursive(one, two, m, n, dp);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int editDistRecursive(String one, String two, int i, int j, int[][] dp) {
        // Базовые случаи
        if (i == 0) return j; // Если первая строка пуста, нужно j вставок
        if (j == 0) return i; // Если вторая строка пуста, нужно i удалений

        // Если уже вычислено, возвращаем значение
        if (dp[i][j] != -1) return dp[i][j];

        // Если символы совпадают, рекурсивно вычисляем для оставшихся строк
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            dp[i][j] = editDistRecursive(one, two, i - 1, j - 1, dp);
        } else {
            // Вычисляем три операции:
            // 1. Замена символа
            int replace = editDistRecursive(one, two, i - 1, j - 1, dp) + 1;
            // 2. Удаление символа из первой строки
            int delete = editDistRecursive(one, two, i - 1, j, dp) + 1;
            // 3. Вставка символа во вторую строку
            int insert = editDistRecursive(one, two, i, j - 1, dp) + 1;

            // Берем минимальное значение
            dp[i][j] = Math.min(replace, Math.min(delete, insert));
        }

        return dp[i][j];
    }

    int result;

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
