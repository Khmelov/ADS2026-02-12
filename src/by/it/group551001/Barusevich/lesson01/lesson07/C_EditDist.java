package by.it.group551001.Barusevich.lesson01.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        int[][] dp = new int[n + 1][m + 1];
        String[][] ops = new String[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
            if (i > 0) ops[i][0] = "-" + one.charAt(i - 1);
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
            if (j > 0) ops[0][j] = "+" + two.charAt(j - 1);
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    ops[i][j] = "#" + one.charAt(i - 1);
                } else {
                    int insert = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;
                    int replace = dp[i - 1][j - 1] + 1;

                    if (insert <= delete && insert <= replace) {
                        dp[i][j] = insert;
                        ops[i][j] = "+" + two.charAt(j - 1);
                    } else if (delete <= insert && delete <= replace) {
                        dp[i][j] = delete;
                        ops[i][j] = "-" + one.charAt(i - 1);
                    } else {
                        dp[i][j] = replace;
                        ops[i][j] = "~" + two.charAt(j - 1);
                    }
                }
            }
        }

        StringBuilder result = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && ops[i][j] != null && ops[i][j].charAt(0) == '#') {
                result.insert(0, ops[i][j] + ",");
                i--; j--;
            } else if (i > 0 && j > 0 && ops[i][j] != null && ops[i][j].charAt(0) == '~') {
                result.insert(0, ops[i][j] + ",");
                i--; j--;
            } else if (i > 0 && ops[i][j] != null && ops[i][j].charAt(0) == '-') {
                result.insert(0, ops[i][j] + ",");
                i--;
            } else if (j > 0 && ops[i][j] != null && ops[i][j].charAt(0) == '+') {
                result.insert(0, ops[i][j] + ",");
                j--;
            } else if (i > 0 && j == 0) {
                result.insert(0, ops[i][0] + ",");
                i--;
            } else if (j > 0 && i == 0) {
                result.insert(0, ops[0][j] + ",");
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