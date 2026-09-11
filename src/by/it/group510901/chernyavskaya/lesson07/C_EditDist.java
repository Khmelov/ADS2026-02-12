package by.it.group510901.chernyavskaya.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int m = one.length();  // длина первой строки
        int n = two.length();  // длина второй строки

        // Таблица для расстояний Левенштейна
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация: превращение в пустую строку
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;  // удалить i символов
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;  // вставить j символов
        }

        // Заполняем таблицу
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если символы равны, замена не нужна (0), иначе нужна (1)
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // Минимум из трех операций:
                // удаление, вставка, замена/копирование
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,     // удаление
                                dp[i][j - 1] + 1),    // вставка
                        dp[i - 1][j - 1] + cost        // замена или копирование
                );
            }
        }

        // Восстанавливаем последовательность операций
        StringBuilder result = new StringBuilder();
        int i = m;
        int j = n;

        // Идем от конца к началу
        while (i > 0 || j > 0) {
            // Если одна из строк закончилась
            if (i == 0) {
                // Только вставки
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
            else if (j == 0) {
                // Только удаления
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
            // Если символы равны - копирование
            else if (one.charAt(i - 1) == two.charAt(j - 1)) {
                result.insert(0, "#,");
                i--;
                j--;
            }
            // Если пришли по диагонали с заменой
            else if (dp[i][j] == dp[i - 1][j - 1] + 1) {
                result.insert(0, "~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            }
            // Если пришли сверху - удаление
            else if (dp[i][j] == dp[i - 1][j] + 1) {
                result.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            }
            // Иначе - вставка
            else {
                result.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
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