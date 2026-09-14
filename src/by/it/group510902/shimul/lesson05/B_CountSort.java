package by.it.group510902.shimul.lesson05;

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

import java.io.*;
import java.util.*;

import java.io.*;
import java.util.*;

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

        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // ---------- Counting Sort ----------
        int MAX_VALUE = 10;
        int[] count = new int[MAX_VALUE + 1];

        // 1. Подсчет количества каждого числа
        for (int i = 0; i < n; i++) {
            count[points[i]]++;
        }

        // 2. Перезапись массива в отсортированном виде
        int index = 0;
        for (int value = 0; value <= MAX_VALUE; value++) {
            while (count[value] > 0) {
                points[index++] = value;
                count[value]--;
            }
        }

        return points;
    }
}
