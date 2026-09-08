package by.it.group510902.gordeev.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    private int[][] dp;
    private String one;
    private String two;

    // Рекурсивное вычисление расстояния Левенштейна
    private int editDist(int i, int j) {
        // Базовые случаи: одна из строк пуста
        if (i == 0) return j;  // нужно вставить j символов
        if (j == 0) return i;  // нужно удалить i символов

        // Мемоизация: если уже считали, возвращаем
        if (dp[i][j] != -1) return dp[i][j];

        // Если символы совпадают, дополнительных операций не нужно
        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            dp[i][j] = editDist(i - 1, j - 1);
            return dp[i][j];
        }

        // Иначе выбираем минимальную стоимость из трёх операций
        int deletion = editDist(i - 1, j) + 1;      // удаление
        int insertion = editDist(i, j - 1) + 1;     // вставка
        int substitution = editDist(i - 1, j - 1) + 1; // замена

        dp[i][j] = Math.min(deletion, Math.min(insertion, substitution));
        return dp[i][j];
    }

    int getDistanceEdinting(String one, String two) {
        this.one = one;
        this.two = two;

        int n = one.length();
        int m = two.length();
        dp = new int[n + 1][m + 1];

        // Инициализация массива -1 (значение не вычислено)
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = -1;
            }
        }

        return editDist(n, m);
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