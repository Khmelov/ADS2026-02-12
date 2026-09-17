package by.it.group510901.artykov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Итерационное вычисление
 * расстояния Левенштейна.
 *
 * Нужно найти минимальное
 * количество операций,
 * необходимых для преобразования
 * одной строки в другую.
 *
 * Разрешённые операции:
 * 1. Вставка
 * 2. Удаление
 * 3. Замена
 *
 * Используется:
 * динамическое программирование.
 *
 * Сложность:
 * O(n * m)
 */

public class B_EditDist {

    /*
     * Метод вычисляет
     * расстояние Левенштейна
     * между двумя строками.
     */
    int getDistanceEdinting(String one, String two) {

        // Длина первой строки
        int n = one.length();

        // Длина второй строки
        int m = two.length();

        /*
         * Таблица динамики.
         *
         * dp[i][j] =
         * минимальное количество операций
         * для преобразования:
         *
         * one[0...i]
         * в
         * two[0...j]
         */
        int[][] dp = new int[n + 1][m + 1];

        // =====================================================
        // Заполнение первой строки
        // =====================================================

        /*
         * Если первая строка пустая,
         * нужно вставить все символы
         */
        for (int j = 0; j <= m; j++) {

            dp[0][j] = j;
        }

        // =====================================================
        // Заполнение первого столбца
        // =====================================================

        /*
         * Если вторая строка пустая,
         * нужно удалить все символы
         */
        for (int i = 0; i <= n; i++) {

            dp[i][0] = i;
        }

        // =====================================================
        // Основной алгоритм
        // =====================================================

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

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
                                dp[i - 1][j] + 1,
                                dp[i][j - 1] + 1
                        ),

                        dp[i - 1][j - 1] + cost
                );
            }
        }

        // Ответ в правом нижнем углу
        return dp[n][m];
    }

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                B_EditDist.class.getResourceAsStream("dataABC.txt");

        B_EditDist instance =
                new B_EditDist();

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