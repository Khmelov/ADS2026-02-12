package by.it.group510901.parkhomenko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Решение задачи C: восстановление редакционного предписания для расстояния Левенштейна.
public class C_EditDist {

    // Возвращает строку операций: # - совпадение, + - вставка, - - удаление, ~ - замена.
    String getDistanceEdinting(String one, String two) {
        int[][] dp = buildTable(one, two);
        return restoreOperations(one, two, dp);
    }

    // Строит таблицу расстояний между всеми парами префиксов строк.
    private int[][] buildTable(String one, String two) {
        int[][] dp = new int[one.length() + 1][two.length() + 1];

        for (int i = 0; i <= one.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= two.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= one.length(); i++) {
            for (int j = 1; j <= two.length(); j++) {
                int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;
                int insert = dp[i][j - 1] + 1;
                int delete = dp[i - 1][j] + 1;
                int replace = dp[i - 1][j - 1] + cost;
                dp[i][j] = Math.min(Math.min(insert, delete), replace);
            }
        }
        return dp;
    }

    // Восстанавливает один оптимальный путь по DP-таблице.
    private String restoreOperations(String one, String two, int[][] dp) {
        StringBuilder reversed = new StringBuilder();
        int i = one.length();
        int j = two.length();

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1) && dp[i][j] == dp[i - 1][j - 1]) {
                reversed.insert(0, "#,");
                i--;
                j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                reversed.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                reversed.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            } else {
                reversed.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }
        return reversed.toString();
    }

    // Запускает решение на трех парах строк из dataABC.txt.
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
