package by.it.group551002.kurzhalov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int m = one.length();
        int n = two.length();

        // Create DP table with (m+1) x (n+1) dimensions
        int[][] dp = new int[m + 1][n + 1];

        // Initialize first column (delete operations)
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        // Initialize first row (insert operations)
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Fill the DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // If characters are equal, no operation needed
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Take minimum of three operations:
                    // 1. Delete: dp[i-1][j] + 1
                    // 2. Insert: dp[i][j-1] + 1
                    // 3. Replace: dp[i-1][j-1] + 1
                    dp[i][j] = Math.min(
                            dp[i - 1][j] + 1,      // delete
                            Math.min(
                                    dp[i][j - 1] + 1,  // insert
                                    dp[i - 1][j - 1] + 1 // replace
                            )
                    );
                }
            }
        }

        // The result is in the bottom-right cell
        int result = dp[m][n];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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