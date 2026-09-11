package by.it.group551004.yakhnin.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    // Рекурсивный метод с мемоизацией
    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // Создаем таблицу для мемоизации (запоминания результатов)
        // dp[i][j] = расстояние Левенштейна для первых i символов one и первых j символов two
        // Инициализируем -1 (означает, что значение еще не посчитано)
        int[][] memo = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;
            }
        }

        return editDist(one, two, n, m, memo);
    }

    private int editDist(String one, String two, int i, int j, int[][] memo) {
        // Базовые случаи
        if (i == 0) {
            return j;  // нужно j вставок
        }
        if (j == 0) {
            return i;  // нужно i удалений
        }

        // Если уже считали, возвращаем запомненное значение
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        // Если символы совпадают, ничего не делаем
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = editDist(one, two, i - 1, j - 1, memo);
        } else {
            // Пробуем три операции и берем минимальную
            int insert = editDist(one, two, i, j - 1, memo);      // вставка
            int delete = editDist(one, two, i - 1, j, memo);      // удаление
            int replace = editDist(one, two, i - 1, j - 1, memo); // замена

            memo[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
        }

        return memo[i][j];
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