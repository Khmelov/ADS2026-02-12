package by.it.group510901.artykov.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Наибольшая невозрастающая подпоследовательность.
 *
 * Нужно найти:
 * 1. Максимальную длину подпоследовательности
 * 2. Индексы элементов
 *
 * Условие:
 * A[i1] >= A[i2] >= A[i3] ...
 *
 * Используется:
 * динамическое программирование.
 *
 * Сложность:
 * O(n^2)
 */

public class C_LongNotUpSubSeq {

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");

        C_LongNotUpSubSeq instance =
                new C_LongNotUpSubSeq();

        int result =
                instance.getNotUpSeqSize(stream);

        System.out.print(result);
    }

    /*
     * Метод находит длину
     * наибольшей невозрастающей
     * подпоследовательности.
     */
    int getNotUpSeqSize(InputStream stream)
            throws FileNotFoundException {

        // Чтение данных
        Scanner scanner = new Scanner(stream);

        // =====================================================
        // Чтение массива
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
         * невозрастающей подпоследовательности,
         * заканчивающейся в i
         */
        int[] dp = new int[n];

        /*
         * prev[i] =
         * индекс предыдущего элемента
         * в подпоследовательности
         */
        int[] prev = new int[n];

        // Начальные значения
        for (int i = 0; i < n; i++) {

            dp[i] = 1;

            prev[i] = -1;
        }

        // Максимальная длина
        int result = 1;

        // Индекс конца последовательности
        int lastIndex = 0;

        /*
         * Проверяем все пары элементов
         */
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < i; j++) {

                /*
                 * Если последовательность
                 * остаётся невозрастающей
                 */
                if (m[j] >= m[i]
                        && dp[j] + 1 > dp[i]) {

                    dp[i] = dp[j] + 1;

                    prev[i] = j;
                }
            }

            /*
             * Обновляем максимум
             */
            if (dp[i] > result) {

                result = dp[i];

                lastIndex = i;
            }
        }

        // =====================================================
        // Восстановление индексов
        // =====================================================

        int[] indexes = new int[result];

        int pos = result - 1;

        while (lastIndex != -1) {

            /*
             * +1 потому что
             * индексация по условию с 1
             */
            indexes[pos] = lastIndex + 1;

            pos--;

            lastIndex = prev[lastIndex];
        }

        // =====================================================
        // Вывод индексов
        // =====================================================

        System.out.println(result);

        for (int index : indexes) {

            System.out.print(index + " ");
        }

        System.out.println();

        return result;
    }
}