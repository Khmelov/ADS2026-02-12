package by.it.group551001.anenko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

public class A_EditDist {

    int[][] memo;

    int getDistanceEdinting(String one, String two) {
        memo = new int[one.length() + 1][two.length() + 1];
        for (int[] row : memo) Arrays.fill(row, -1);
        return recursiveEditDist(one, two, one.length(), two.length());
    }

    private int recursiveEditDist(String s1, String s2, int m, int n) {
        if (m == 0) return n;
        if (n == 0) return m;
        if (memo[m][n] != -1) return memo[m][n];

        if (s1.charAt(m - 1) == s2.charAt(n - 1)) {
            return memo[m][n] = recursiveEditDist(s1, s2, m - 1, n - 1);
        }

        int insert = recursiveEditDist(s1, s2, m, n - 1);
        int delete = recursiveEditDist(s1, s2, m - 1, n);
        int replace = recursiveEditDist(s1, s2, m - 1, n - 1);

        return memo[m][n] = 1 + Math.min(Math.min(insert, delete), replace);
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        if (scanner.hasNextLine()) System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        if (scanner.hasNextLine()) System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        if (scanner.hasNextLine()) System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}