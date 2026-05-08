package by.it.group551004.podvitelskiymichael.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1],
                            Math.min(
                                    dp[i - 1][j],
                                    dp[i][j - 1]
                            )
                    );
                }
            }
        }

        StringBuilder ops = new StringBuilder();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                ops.append("#,");
                i--; j--;
            } else
                if (j > 0 && (i == 0 || dp[i][j - 1] < dp[i - 1][j - 1] && dp[i][j - 1] <= dp[i - 1][j]
                    || i > 0 && dp[i][j] == dp[i][j - 1] + 1 && dp[i][j - 1] <= dp[i - 1][j] && dp[i][j - 1] < dp[i - 1][j - 1])) {

                ops.append("+").append(two.charAt(j - 1)).append(",");
                j--;
            } else
                if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                ops.append("~").append(two.charAt(j - 1)).append(",");
                i--; j--;
            } else
                if (i > 0 && (j == 0 || dp[i - 1][j] <= dp[i][j - 1])) {
                ops.append("-").append(one.charAt(i - 1)).append(",");
                i--;
            } else {
                ops.append("+").append(two.charAt(j - 1)).append(",");
                j--;
            }
        }

        String[] parts = ops.toString().split(",", -1);
        StringBuilder result = new StringBuilder();
        for (int k = parts.length - 1; k >= 0; k--) {
            if (!parts[k].isEmpty()) {
                result.append(parts[k]).append(",");
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