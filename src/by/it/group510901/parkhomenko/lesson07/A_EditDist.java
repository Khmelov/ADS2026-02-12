package by.it.group510901.parkhomenko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

// Решение задачи A: рекурсивное расстояние Левенштейна с мемоизацией.
public class A_EditDist {

    // Возвращает минимальное количество вставок, удалений и замен для преобразования one в two.
    int getDistanceEdinting(String one, String two) {
        int[][] memo = new int[one.length() + 1][two.length() + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return calc(one, two, one.length(), two.length(), memo);
    }

    // Рекурсивно считает расстояние для первых i символов one и первых j символов two.
    private int calc(String one, String two, int i, int j, int[][] memo) {
        if (i == 0) return j;
        if (j == 0) return i;
        if (memo[i][j] != -1) return memo[i][j];

        int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;
        int insert = calc(one, two, i, j - 1, memo) + 1;
        int delete = calc(one, two, i - 1, j, memo) + 1;
        int replace = calc(one, two, i - 1, j - 1, memo) + cost;

        memo[i][j] = Math.min(Math.min(insert, delete), replace);
        return memo[i][j];
    }

    // Запускает решение на трех парах строк из dataABC.txt.
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
