package by.it.group510901.kaleeva.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // Таблица для хранения минимальных расстояний
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация первой строки и первого столбца
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        // Заполняем таблицу
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char ch1 = one.charAt(i - 1);
                char ch2 = two.charAt(j - 1);

                if (ch1 == ch2) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int deleteOp = dp[i - 1][j] + 1;
                    int insertOp = dp[i][j - 1] + 1;
                    int replaceOp = dp[i - 1][j - 1] + 1;
                    dp[i][j] = Math.min(deleteOp, Math.min(insertOp, replaceOp));
                }
            }
        }

        // Восстанавливаем редакционное предписание (список операций)
        // Идём обратным ходом из правого нижнего угла в левый верхний
        int i = n;
        int j = m;
        StringBuilder operations = new StringBuilder();

        while (i > 0 || j > 0) {
            // Случай 1: символы совпадают
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                // Операция "#" - копирование (оставить как есть)
                operations.insert(0, "#,");
                i--;
                j--;
            }
            // Случай 2: пришли из замены
            else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                // Операция "~" - замена символа
                operations.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            }
            // Случай 3: пришли из удаления
            else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                // Операция "-" - удаление символа из первой строки
                operations.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
            // Случай 4: пришли из вставки
            else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                // Операция "+" - вставка символа из второй строки
                operations.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }

        // Возвращаем строку с операциями
        return operations.toString();
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