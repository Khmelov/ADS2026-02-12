package by.it.group510902.gordeev.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int n = one.length();
        int m = two.length();

        // Таблица DP
        int[][] dp = new int[n + 1][m + 1];

        // Массив для хранения операций (0 - копирование, 1 - замена, 2 - удаление, 3 - вставка)
        int[][] op = new int[n + 1][m + 1];

        // Инициализация базовых случаев
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            op[0][j] = 3; // вставка
        }

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            op[i][0] = 2; // удаление
        }

        // Заполнение таблиц
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    op[i][j] = 0; // копирование
                } else {
                    int deletion = dp[i - 1][j] + 1;
                    int insertion = dp[i][j - 1] + 1;
                    int substitution = dp[i - 1][j - 1] + 1;

                    dp[i][j] = Math.min(deletion, Math.min(insertion, substitution));

                    if (dp[i][j] == substitution) {
                        op[i][j] = 1; // замена
                    } else if (dp[i][j] == deletion) {
                        op[i][j] = 2; // удаление
                    } else {
                        op[i][j] = 3; // вставка
                    }
                }
            }
        }

        // Восстановление редакционного предписания (идем с конца)
        StringBuilder resultBuilder = new StringBuilder();
        int i = n, j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && op[i][j] == 0) { // копирование
                resultBuilder.insert(0, "#,");
                i--;
                j--;
            } else if (i > 0 && j > 0 && op[i][j] == 1) { // замена
                resultBuilder.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            } else if (i > 0 && op[i][j] == 2) { // удаление
                resultBuilder.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            } else { // вставка
                resultBuilder.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }

        String result = resultBuilder.toString();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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