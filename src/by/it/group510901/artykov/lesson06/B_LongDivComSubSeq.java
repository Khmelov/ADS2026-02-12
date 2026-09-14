package by.it.group510901.artykov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Наибольшая кратная подпоследовательность.
 *
 * Нужно найти максимальную длину
 * подпоследовательности,
 * в которой каждый следующий элемент
 * делится на предыдущий.
 *
 * Используется:
 * динамическое программирование.
 *
 * Сложность:
 * O(n^2)
 */

public class B_LongDivComSubSeq {

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");

        B_LongDivComSubSeq instance =
                new B_LongDivComSubSeq();

        int result =
                instance.getDivSeqSize(stream);

        System.out.print(result);
    }

    /*
     * Метод находит длину
     * наибольшей кратной
     * подпоследовательности.
     */
    int getDivSeqSize(InputStream stream)
            throws FileNotFoundException {

        // Чтение данных
        Scanner scanner = new Scanner(stream);

        // =====================================================
        // Чтение последовательности
        // =====================================================

        // Размер массива
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
         * длина максимальной
         * кратной подпоследовательности,
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
                 * Если текущий элемент
                 * делится на предыдущий
                 */
                if (m[i] % m[j] == 0) {

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