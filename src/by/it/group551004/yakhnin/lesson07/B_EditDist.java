package by.it.group551004.yakhnin.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // Создаем таблицу (n+1) x (m+1)
        int[][] dp = new int[n + 1][m + 1];

        // Базовые случаи: превращение в пустую строку
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;  // удалить i символов
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;  // вставить j символов
        }

        // Заполняем таблицу итеративно
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    // Символы совпадают — стоимость не увеличивается
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Минимум из трех операций + 1
                    int insert = dp[i][j - 1];
                    int delete = dp[i - 1][j];
                    int replace = dp[i - 1][j - 1];
                    dp[i][j] = 1 + Math.min(insert, Math.min(delete, replace));
                }
            }
        }

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