package by.it.group551003.ogonovsky.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        // ради экономии памяти пусть two - короткая строка (по ней пойдут столбцы)
        if (one.length() < two.length()) {
            String tmp = one; one = two; two = tmp;
        }

        int n = one.length();
        int m = two.length();

        // храним только две строки таблицы DP
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        // базовый случай: пустой префикс one против префиксов two длины j
        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        // итерационное заполнение по строкам
        for (int i = 1; i <= n; i++) {
            curr[0] = i; // пустой префикс two против префикса one длины i
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    int del = prev[j];     // удаление символа из one
                    int ins = curr[j - 1]; // вставка символа в one
                    int rep = prev[j - 1]; // замена
                    curr[j] = 1 + Math.min(del, Math.min(ins, rep));
                }
            }
            // меняем строки местами: curr становится prev для следующей итерации
            int[] tmp = prev; prev = curr; curr = tmp;
        }

        return prev[m];
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