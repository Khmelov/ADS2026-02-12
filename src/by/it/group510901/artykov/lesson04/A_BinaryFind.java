package by.it.group510901.artykov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Бинарный поиск в отсортированном массиве.
 *
 * Для каждого числа нужно:
 * - найти его индекс
 * - или вернуть -1
 *
 * Сложность:
 * O(log n)
 */

public class A_BinaryFind {

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                A_BinaryFind.class.getResourceAsStream("dataA.txt");

        A_BinaryFind instance = new A_BinaryFind();

        int[] result = instance.findIndex(stream);

        // Вывод результата
        for (int index : result) {

            System.out.print(index + " ");
        }
    }

    /*
     * Метод выполняет бинарный поиск
     * для всех чисел из входных данных.
     */
    public int[] findIndex(InputStream stream)
            throws FileNotFoundException {

        // Чтение данных
        Scanner scanner = new Scanner(stream);

        // Размер массива
        int n = scanner.nextInt();

        // Отсортированный массив
        int[] a = new int[n];

        // Заполнение массива
        for (int i = 0; i < n; i++) {

            a[i] = scanner.nextInt();
        }

        // Количество запросов
        int k = scanner.nextInt();

        // Массив ответов
        int[] result = new int[k];

        /*
         * Поиск каждого элемента
         */
        for (int i = 0; i < k; i++) {

            int value = scanner.nextInt();

            // Левая граница
            int left = 0;

            // Правая граница
            int right = n - 1;

            // Индекс найденного элемента
            int foundIndex = -1;

            /*
             * Бинарный поиск
             */
            while (left <= right) {

                // Середина
                int mid = (left + right) / 2;

                // Элемент найден
                if (a[mid] == value) {

                    // +1 из-за условия задачи
                    foundIndex = mid + 1;

                    break;
                }

                // Ищем справа
                if (a[mid] < value) {

                    left = mid + 1;

                } else {

                    // Ищем слева
                    right = mid - 1;
                }
            }

            // Сохраняем результат
            result[i] = foundIndex;
        }

        return result;
    }
}