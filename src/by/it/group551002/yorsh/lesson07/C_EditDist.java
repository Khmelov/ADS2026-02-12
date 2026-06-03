package by.it.group551002.yorsh.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int m = one.length();
        int n = two.length();

        // таблица минимальных стоимостей
        int[][] dp = new int[m + 1][n + 1];

        // базовые случаи
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        // заполняем dp итеративно
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(dp[i - 1][j] + 1,
                        Math.min(dp[i][j - 1] + 1,
                                dp[i - 1][j - 1] + cost));
            }
        }

        // восстановление редакционного предписания
        // идем из правого нижнего угла в левый верхний
        List<String> operations = new ArrayList<>();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0
                    && one.charAt(i - 1) == two.charAt(j - 1)
                    && dp[i][j] == dp[i - 1][j - 1]) {
                // символы совпали – копирование
                operations.add("#");
                i--;
                j--;
            } else if (i > 0 && j > 0
                    && dp[i][j] == dp[i - 1][j - 1] + 1) {
                // замена символа
                operations.add("~" + two.charAt(j - 1));
                i--;
                j--;
            } else if (i > 0
                    && dp[i][j] == dp[i - 1][j] + 1) {
                // удаление символа из первой строки
                operations.add("-" + one.charAt(i - 1));
                i--;
            } else {
                // вставка символа из второй строки
                operations.add("+" + two.charAt(j - 1));
                j--;
            }
        }

        // порядок операций в списке обратный – разворачиваем
        Collections.reverse(operations);

        // формируем строку с запятыми (в конце тоже запятая)
        StringBuilder result = new StringBuilder();
        for (String op : operations) {
            result.append(op).append(",");
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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