package by.it.group551002.litovchenko.lesson05;

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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        // !!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        // размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // читаем точки
        // нам нужно найти минимальное и максимальное значение, чтобы знать размер
        // вспомогательного массива
        // по условию задачи числа натуральные и не превышают 10
        int min = 1;
        int max = 10;

        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // тут реализуйте логику задачи с применением сортировки подсчетом

        // Создаем вспомогательный массив для подсчета количества вхождений каждого
        // числа
        // Размер max - min + 1 (в нашем случае 10)
        int[] count = new int[max - min + 1];

        // 1. Считаем количество вхождений каждого числа
        for (int value : points) {
            count[value - min]++;
        }

        // 2. Перезаписываем исходный массив на основе данных из массива подсчета
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            // Пока количество данного числа (i + min) больше нуля
            while (count[i] > 0) {
                points[index++] = i + min;
                count[i]--;
            }
        }

        // !!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }
}
