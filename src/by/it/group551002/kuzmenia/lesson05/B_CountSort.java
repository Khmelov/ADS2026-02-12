package by.it.group551002.kuzmenia.lesson05;

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
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] points = new int[n];

        // массив для подсчета встретившихся чисел (от 0 до 10)
        int[] count = new int[11];

        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();

            // считаем сколько раз выпало каждое число
            count[points[i]]++;
        }

        // перезаписываем исходный массив на основе подсчитанных данных
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            // пока количество встретившихся чисел i не исчерпано
            while (count[i] > 0)
            {
                points[index++] = i;
                count[i]--;
            }
        }
        return points;
    }

}
