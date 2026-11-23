package by.it.group510902.vishnevskaya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте  линейный метод со сложностью O(n)

Пример: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
Вольный перевод: http://programador.ru/sorting-positive-int-linear-time/
*/
public class B_CountSort { /*сортировка подсчетом */

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }
/*читает кол-во числен а потом сами числа н*/
    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] points = new int[n];
/*сортирует числа с 1 до 10*/
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
/*считатет сколько раз встретилось каждое число потом переписывает массив сначала единицы потом 2 и тд*/
        int[] count = new int[11];
        for (int value : points) {
            count[value]++;
        }

        int index = 0;
        for (int value = 1; value <= 10; value++) {
            for (int j = 0; j < count[value]; j++) {
                points[index++] = value;
            }
        }

        return points;
    }

}