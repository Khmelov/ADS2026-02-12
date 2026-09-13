package by.it.group510901.artykov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Расстояние Левенштейна.
 *
 * Нужно найти минимальное количество
 * операций для преобразования
 * одной строки в другую.
 *
 * Разрешённые операции:
 * 1. Вставка
 * 2. Удаление
 * 3. Замена
 *
 * Используется:
 * рекурсия + динамическое программирование.
 */

public class A_EditDist {

    /*
     * Таблица для сохранения
     * уже вычисленных значений.
     */
    private int[][] dp;

    /*
     * Основной метод вычисления
     * расстояния Левенштейна.
     */
    int getDistanceEdinting(String one, String two) {

        // Размеры строк
        int n = one.length();

        int m = two.length();

        /*
         * Создание таблицы памяти.
         */
        dp = new int[n + 1][m + 1];

        /*
         * Заполняем таблицу
         * значением -1
         */
        for (int i = 0; i <= n; i++) {

            for (int j = 0; j <= m; j++) {

                dp[i][j] = -1;
            }
        }

        /*
         * Запуск рекурсивного алгоритма
         */
        return levenshtein(one, two, n, m);
    }

    /*
     * Рекурсивный метод
     * вычисления расстояния.
     *
     * i - длина первой строки
     * j - длина второй строки
     */
    int levenshtein(
            String one,
            String two,
            int i,
            int j
    ) {

        /*
         * Если значение уже вычислено,
         * возвращаем его
         */
        if (dp[i][j] != -1) {

            return dp[i][j];
        }

        /*
         * Если первая строка пустая,
         * нужно вставить все символы
         */
        if (i == 0) {

            dp[i][j] = j;

            return dp[i][j];
        }

        /*
         * Если вторая строка пустая,
         * нужно удалить все символы
         */
        if (j == 0) {

            dp[i][j] = i;

            return dp[i][j];
        }

        /*
         * Стоимость замены:
         * 0 если символы равны
         * 1 если разные
         */
        int cost;

        if (one.charAt(i - 1)
                == two.charAt(j - 1)) {

            cost = 0;

        } else {

            cost = 1;
        }

        /*
         * Минимум из:
         * 1. Удаление
         * 2. Вставка
         * 3. Замена
         */
        dp[i][j] = Math.min(

                Math.min(
                        levenshtein(one, two, i - 1, j) + 1,
                        levenshtein(one, two, i, j - 1) + 1
                ),

                levenshtein(one, two, i - 1, j - 1) + cost
        );

        return dp[i][j];
    }

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                A_EditDist.class.getResourceAsStream("dataABC.txt");

        A_EditDist instance =
                new A_EditDist();

        Scanner scanner = new Scanner(stream);

        System.out.println(
                instance.getDistanceEdinting(
                        scanner.nextLine(),
                        scanner.nextLine()
                )
        );

        System.out.println(
                instance.getDistanceEdinting(
                        scanner.nextLine(),
                        scanner.nextLine()
                )
        );

        System.out.println(
                instance.getDistanceEdinting(
                        scanner.nextLine(),
                        scanner.nextLine()
                )
        );
    }
}