package by.it.group510901.artykov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Редакционное предписание
 * для преобразования строк.
 *
 * Используется:
 * расстояние Левенштейна
 * и динамическое программирование.
 *
 * Обозначения:
 * +x  -> вставка символа x
 * -x  -> удаление символа x
 * ~x  -> замена символом x
 * #   -> символы совпадают
 */

public class C_EditDist {

    /*
     * Метод строит
     * редакционное предписание
     * для преобразования строк.
     */
    String getDistanceEdinting(String one, String two) {

        // Длины строк
        int n = one.length();

        int m = two.length();

        /*
         * Таблица расстояний.
         */
        int[][] dp = new int[n + 1][m + 1];

        // =====================================================
        // Заполнение границ таблицы
        // =====================================================

        for (int i = 0; i <= n; i++) {

            dp[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {

            dp[0][j] = j;
        }

        // =====================================================
        // Заполнение таблицы
        // =====================================================

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                // Стоимость замены
                int cost;

                if (one.charAt(i - 1)
                        == two.charAt(j - 1)) {

                    cost = 0;

                } else {

                    cost = 1;
                }

                /*
                 * Минимум из:
                 * удаление
                 * вставка
                 * замена
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

        // =====================================================
        // Восстановление ответа
        // =====================================================

        StringBuilder result =
                new StringBuilder();

        int i = n;
        int j = m;

        /*
         * Идём из конца таблицы
         * к началу
         */
        while (i > 0 || j > 0) {

            /*
             * Совпадение символов
             */
            if (i > 0
                    && j > 0
                    && one.charAt(i - 1)
                    == two.charAt(j - 1)
                    && dp[i][j]
                    == dp[i - 1][j - 1]) {

                result.insert(0, "#,");

                i--;
                j--;
            }

            /*
             * Замена символа
             */
            else if (i > 0
                    && j > 0
                    && dp[i][j]
                    == dp[i - 1][j - 1] + 1) {

                result.insert(
                        0,
                        "~" + two.charAt(j - 1) + ","
                );

                i--;
                j--;
            }

            /*
             * Вставка символа
             */
            else if (j > 0
                    && dp[i][j]
                    == dp[i][j - 1] + 1) {

                result.insert(
                        0,
                        "+" + two.charAt(j - 1) + ","
                );

                j--;
            }

            /*
             * Удаление символа
             */
            else {

                result.insert(
                        0,
                        "-" + one.charAt(i - 1) + ","
                );

                i--;
            }
        }

        return result.toString();
    }

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                C_EditDist.class.getResourceAsStream("dataABC.txt");

        C_EditDist instance =
                new C_EditDist();

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