package by.it.group510901.filimonova.lesson01.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();

        // Создаем таблицы DP и для хранения операций
        int[][] dp = new int[m + 1][n + 1];
        String[][] op = new String[m + 1][n + 1];

        // Инициализация
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            if (i > 0) {
                op[i][0] = "-" + one.charAt(i - 1);
            }
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
            if (j > 0) {
                op[0][j] = "+" + two.charAt(j - 1);
            }
        }

        // Заполняем таблицы
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char c1 = one.charAt(i - 1);
                char c2 = two.charAt(j - 1);

                if (c1 == c2) {
                    // Символы совпадают - копирование (без символа!)
                    dp[i][j] = dp[i - 1][j - 1];
                    op[i][j] = "#";  // ТОЛЬКО "#", без символа
                } else {
                    int delete = dp[i - 1][j] + 1;
                    int insert = dp[i][j - 1] + 1;
                    int replace = dp[i - 1][j - 1] + 1;

                    if (delete <= insert && delete <= replace) {
                        dp[i][j] = delete;
                        op[i][j] = "-" + c1;
                    } else if (insert <= delete && insert <= replace) {
                        dp[i][j] = insert;
                        op[i][j] = "+" + c2;
                    } else {
                        dp[i][j] = replace;
                        op[i][j] = "~" + c2;
                    }
                }
            }
        }

        // Восстанавливаем последовательность операций
        List<String> operations = new ArrayList<>();
        int i = m;
        int j = n;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && op[i][j] != null) {
                String operation = op[i][j];
                operations.add(0, operation);

                if (operation.equals("#") || operation.startsWith("~")) {
                    i--;
                    j--;
                } else if (operation.startsWith("-")) {
                    i--;
                } else if (operation.startsWith("+")) {
                    j--;
                }
            } else if (i > 0) {
                operations.add(0, "-" + one.charAt(i - 1));
                i--;
            } else if (j > 0) {
                operations.add(0, "+" + two.charAt(j - 1));
                j--;
            }
        }

        // Формируем строку результата
        StringBuilder result = new StringBuilder();
        for (String operation : operations) {
            result.append(operation).append(",");
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