package by.it.group510901.artykov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Наибольшая возрастающая подпоследовательность.
 *
 * Нужно найти максимальную длину
 * подпоследовательности,
 * в которой каждый следующий элемент
 * больше предыдущего.
 *
 * Используется:
 * динамическое программирование.
 *
 * Сложность:
 * O(n^2)
 */

public class A_LIS {

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                A_LIS.class.getResourceAsStream("dataA.txt");

        A_LIS instance = new A_LIS();

        int result = instance.getSeqSize(stream);

        System.out.print(result);
    }

    /*
     * Метод находит длину
     * наибольшей возрастающей
     * подпоследовательности.
     */
    int getSeqSize(InputStream stream)
            throws FileNotFoundException {

        // Чтение данных
        Scanner scanner = new Scanner(stream);

        // =====================================================
        // Чтение последовательности
        // =====================================================

        // Длина массива
        int n = scanner.nextInt();

        // Массив чисел
        int[] m = new int[n];

        for (int i = 0; i < n; i++) {

            m[i] = scanner.nextInt();
        }

        // =====================================================
        // Динамическое программирование
        // =====================================================

        /*
         * dp[i] =
         * длина наибольшей возрастающей
         * подпоследовательности,
         * заканчивающейся в i
         */
        int[] dp = new int[n];

        // Минимальная длина = 1
        for (int i = 0; i < n; i++) {

            dp[i] = 1;
        }

        // Максимальный результат
        int result = 1;

        /*
         * Проверяем все пары элементов
         */
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < i; j++) {

                /*
                 * Если можно продолжить
                 * возрастающую последовательность
                 */
                if (m[j] < m[i]) {

                    dp[i] = Math.max(
                            dp[i],
                            dp[j] + 1
                    );
                }
            }

            // Обновляем максимум
            result = Math.max(result, dp[i]);
        }

        return result;
    }
}