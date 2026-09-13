package by.it.group510901.sorokoletov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        int m = one.length();
        int n = two.length();
        Integer[][] memo = new Integer[m + 1][n + 1];
        return editDist(one, two, m, n, memo);
    }

    private int editDist(String one, String two, int i, int j, Integer[][] memo) {
        if (i == 0) return j;
        if (j == 0) return i;

        if (memo[i][j] != null) return memo[i][j];

        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            memo[i][j] = editDist(one, two, i - 1, j - 1, memo);
        } else {
            int insert = editDist(one, two, i, j - 1, memo) + 1;
            int delete = editDist(one, two, i - 1, j, memo) + 1;
            int replace = editDist(one, two, i - 1, j - 1, memo) + 1;
            memo[i][j] = Math.min(Math.min(insert, delete), replace);
        }
        return memo[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}