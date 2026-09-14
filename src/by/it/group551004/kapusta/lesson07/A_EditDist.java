package by.it.group551004.kapusta.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    int[][] memo; // таблица для мемоизации

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        memo = new int[n + 1][m + 1];

        // заполняем -1, чтобы понимать, что значение ещё не вычислено
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;
            }
        }

        return dist(one, two, n, m);
    }

    // рекурсивная функция расстояния Левенштейна
    private int dist(String a, String b, int i, int j) {

        // если уже вычисляли — возвращаем
        if (memo[i][j] != -1) return memo[i][j];

        // если одна строка пустая — нужно j вставок или i удалений
        if (i == 0) return memo[i][j] = j;
        if (j == 0) return memo[i][j] = i;

        int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;

        // три варианта:
        // 1) удаление
        int del = dist(a, b, i - 1, j) + 1;

        // 2) вставка
        int ins = dist(a, b, i, j - 1) + 1;

        // 3) замена
        int rep = dist(a, b, i - 1, j - 1) + cost;

        // выбираем минимум
        return memo[i][j] = Math.min(del, Math.min(ins, rep));
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
