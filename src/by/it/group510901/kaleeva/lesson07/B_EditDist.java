package by.it.group510901.kaleeva.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        int n = one.length();  // длина первой строки
        int m = two.length();  // длина второй строки

        // Создаём таблицу dp размером (n+1) x (m+1)
        // dp[i][j] = минимальное расстояние между первыми i символами one
        // и первыми j символами two
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация: пустая строка и строка из j символов
        // dp[0][j] = j, потому что нужно j раз вставить символ
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        // Инициализация: строка из i символов и пустая строка
        // dp[i][0] = i, потому что нужно i раз удалить символ
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        // Заполняем таблицу построчно
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Текущие символы
                char ch1 = one.charAt(i - 1);
                char ch2 = two.charAt(j - 1);

                // Если символы совпадают
                if (ch1 == ch2) {
                    // Никаких операций не нужно, берём значение из диагонали
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Если символы разные, выбираем минимальную операцию:

                    // Удаление: dp[i-1][j] + 1
                    int deleteOp = dp[i - 1][j] + 1;

                    // Вставка: dp[i][j-1] + 1
                    int insertOp = dp[i][j - 1] + 1;

                    // Замена: dp[i-1][j-1] + 1
                    int replaceOp = dp[i - 1][j - 1] + 1;

                    // Берём минимум
                    dp[i][j] = Math.min(deleteOp, Math.min(insertOp, replaceOp));
                }
            }
        }

        // Ответ находится в правом нижнем углу таблицы
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