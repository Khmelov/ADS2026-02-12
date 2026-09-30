package by.it.group551003.ogonovsky.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        // d[i][j] - расстояние Левенштейна между префиксами one[0..i) и two[0..j)
        int[][] d = new int[n + 1][m + 1];

        // базовые случаи
        for (int i = 0; i <= n; i++) {
            d[i][0] = i; // удалить i символов
        }
        for (int j = 0; j <= m; j++) {
            d[0][j] = j; // вставить j символов
        }

        // основной переход
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    d[i][j] = d[i - 1][j - 1];
                } else {
                    int del = d[i - 1][j];     // удаление из one
                    int ins = d[i][j - 1];     // вставка в one
                    int rep = d[i - 1][j - 1]; // замена
                    d[i][j] = 1 + Math.min(del, Math.min(ins, rep));
                }
            }
        }

        return d[n][m];
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