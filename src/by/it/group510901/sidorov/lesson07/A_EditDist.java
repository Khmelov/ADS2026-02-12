package by.it.group510901.sidorov.lesson07;

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
        int n = one.length();
        int m = two.length();

        // Создаем таблицу для хранения результатов подзадач
        int[][] memo = new int[n + 1][m + 1];

        // Инициализируем таблицу значением -1 (признак того, что подзадача не решена)
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;
            }
        }
        int result = editDistRecursive(one, two, n, m, memo);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private int editDistRecursive(String s1, String s2, int n, int m, int[][] memo) {
        // Базовые случаи: если одна из строк закончилась
        if (n == 0) return m;
        if (m == 0) return n;

        // Если результат уже вычислен, возвращаем его
        if (memo[n][m] != -1) {
            return memo[n][m];
        }

        // Если последние символы одинаковы, идем дальше без увеличения стоимости
        if (s1.charAt(n - 1) == s2.charAt(m - 1)) {
            memo[n][m] = editDistRecursive(s1, s2, n - 1, m - 1, memo);
        } else {
            // Иначе выбираем минимум из трех операций
            int insert = editDistRecursive(s1, s2, n, m - 1, memo);
            int delete = editDistRecursive(s1, s2, n - 1, m, memo);
            int replace = editDistRecursive(s1, s2, n - 1, m - 1, memo);

            memo[n][m] = 1 + Math.min(Math.min(insert, delete), replace);
        }

        return memo[n][m];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
