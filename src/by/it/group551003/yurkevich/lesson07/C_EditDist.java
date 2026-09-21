package by.it.group551003.yurkevich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        int[][] D = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            D[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            D[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost;
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                int del = D[i - 1][j] + 1;
                int ins = D[i][j - 1] + 1;
                int sub = D[i - 1][j - 1] + cost;

                D[i][j] = Math.min(del, Math.min(ins, sub));
            }
        }

        StringBuilder result = new StringBuilder();
        int i = n;
        int j = m;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1)) {
                result.insert(0, "#,");
                i--;
                j--;
            }
            else if (i > 0 && D[i][j] == D[i - 1][j] + 1) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
            else if (j > 0 && D[i][j] == D[i][j - 1] + 1) {
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
            else {
                result.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
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