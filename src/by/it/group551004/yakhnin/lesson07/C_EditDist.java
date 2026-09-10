package by.it.group551004.yakhnin.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        int[][] dp = new int[n + 1][m + 1];
        char[][] op = new char[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            if (i > 0) op[i][0] = '-';  // удаление
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            if (j > 0) op[0][j] = '+';  // вставка
        }

        // Заполняем таблицу
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char c1 = one.charAt(i - 1);
                char c2 = two.charAt(j - 1);

                if (c1 == c2) {
                    // Символы совпадают - копирование (стоимость 0)
                    dp[i][j] = dp[i - 1][j - 1];
                    op[i][j] = '#';
                } else {
                    // Пробуем три операции
                    int insert = dp[i][j - 1];   // вставка
                    int delete = dp[i - 1][j];   // удаление
                    int replace = dp[i - 1][j - 1]; // замена

                    // Выбираем минимальную
                    if (insert <= delete && insert <= replace) {
                        dp[i][j] = insert + 1;
                        op[i][j] = '+';
                    } else if (delete <= insert && delete <= replace) {
                        dp[i][j] = delete + 1;
                        op[i][j] = '-';
                    } else {
                        dp[i][j] = replace + 1;
                        op[i][j] = '~';
                    }
                }
            }
        }

        // Восстанавливаем последовательность операций (идем с конца в начало)
        StringBuilder result = new StringBuilder();
        int i = n, j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && op[i][j] == '#') {

                result.insert(0, "#,");
                i--;
                j--;
            } else if (i > 0 && j > 0 && op[i][j] == '~') {
                result.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            } else
                if (i > 0 && (j == 0 || op[i][j] == '-')) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            } else if (j > 0 && (i == 0 || op[i][j] == '+')) {
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
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