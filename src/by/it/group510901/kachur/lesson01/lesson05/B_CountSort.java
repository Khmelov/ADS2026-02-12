package by.it.group510901.kachur.lesson01.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)

Пример: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
Вольный перевод: http://programador.ru/sorting-positive-int-linear-time/
*/

public class B_CountSort {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        //читаем точки
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением сортировки подсчетом
        //  СОРТИРОВКА ПОДСЧЕТОМ

        // Диапазон чисел: от 1 до 10 (по условию числа натуральные до 10)
        // Создаем массив счетчиков размером 11 (индексы 0-10)
        // Индекс 0 не используем, так как числа ≥ 1
        int MAX_VALUE = 10;
        int[] count = new int[MAX_VALUE + 1];  // +1 чтобы был индекс 10

        // Шаг 1: Подсчитываем количество каждого числа
        for (int i = 0; i < n; i++) {
            int value = points[i];
            count[value]++;  // увеличиваем счетчик для этого числа
        }

        // Шаг 2: Заполняем исходный массив отсортированными значениями
        int index = 0;  // текущая позиция в результирующем массиве
        for (int value = 1; value <= MAX_VALUE; value++) {
            // Добавляем число 'value' столько раз, сколько оно встретилось
            for (int j = 0; j < count[value]; j++) {
                points[index] = value;
                index++;
            }
        }



        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
