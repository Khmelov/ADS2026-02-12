package by.it.group510901.artykov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Сортировка подсчётом (Count Sort).
 *
 * Используется для сортировки чисел
 * с небольшим диапазоном значений.
 *
 * В задаче числа:
 * от 0 до 10.
 *
 * Сложность:
 * O(n)
 */

public class B_CountSort {

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                B_CountSort.class.getResourceAsStream("dataB.txt");

        B_CountSort instance =
                new B_CountSort();

        int[] result = instance.countSort(stream);

        // Вывод результата
        for (int index : result) {

            System.out.print(index + " ");
        }
    }

    /*
     * Метод выполняет
     * сортировку подсчётом.
     */
    int[] countSort(InputStream stream)
            throws FileNotFoundException {

        // Чтение данных
        Scanner scanner = new Scanner(stream);

        // Размер массива
        int n = scanner.nextInt();

        // Исходный массив
        int[] points = new int[n];

        // Чтение массива
        for (int i = 0; i < n; i++) {

            points[i] = scanner.nextInt();
        }

        // =====================================================
        // Сортировка подсчётом
        // =====================================================

        /*
         * Массив для подсчёта чисел.
         *
         * Индекс = число
         * Значение = сколько раз встретилось
         */
        int[] count = new int[11];

        /*
         * Подсчитываем количество
         * каждого числа
         */
        for (int value : points) {

            count[value]++;
        }

        /*
         * Заполняем исходный массив
         * отсортированными значениями
         */
        int index = 0;

        for (int number = 0; number <= 10; number++) {

            /*
             * Пока число встречается
             */
            while (count[number] > 0) {

                points[index] = number;

                index++;

                count[number]--;
            }
        }

        return points;
    }
}