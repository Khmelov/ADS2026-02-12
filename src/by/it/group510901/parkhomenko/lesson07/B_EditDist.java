package by.it.group510901.parkhomenko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Решение задачи B: итерационное расстояние Левенштейна через таблицу динамики.
public class B_EditDist {

    // Возвращает минимальное количество операций редактирования для преобразования one в two.
    int getDistanceEdinting(String one, String two) {
        int[][] dp = buildTable(one, two);
        return dp[one.length()][two.length()];
    }

    // Строит DP-таблицу, где dp[i][j] - расстояние между префиксами длины i и j.
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

    // Запускает решение на трех парах строк из dataABC.txt.
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
