package by.it.group551002.sikorski.lesson05;

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

        // Ищем границы чисел. В условии сказано до 10, но лучше найти максимум
        int min = 1; // По условию натуральные
        int max = 10;

        // Массив для подсчета (размер 11, чтобы индексы были от 0 до 10)
        int[] count = new int[max + 1];

        //читаем точки
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
            count[points[i]]++;
        }
        //тут реализуйте логику задачи с применением сортировки подсчетом
// 2. Перезаписываем исходный массив отсортированными данными
        int index = 0;
        for (int i = min; i <= max; i++) {
            // Пока в "корзине" с числом i есть элементы
            while (count[i] > 0) {
                points[index++] = i;
                count[i]--;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
