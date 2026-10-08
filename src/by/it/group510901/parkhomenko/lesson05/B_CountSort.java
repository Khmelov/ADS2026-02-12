package by.it.group510901.parkhomenko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Задача B.
 * Нужно отсортировать натуральные числа, каждое из которых не больше 10.
 * Благодаря маленькому диапазону значений сортировка подсчетом работает за O(n).
 */
public class B_CountSort {

    private static final int MAX_VALUE = 10;

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
        int[] counts = new int[MAX_VALUE + 1];

        // counts[value] хранит, сколько раз value встретилось во входных данных.
        for (int i = 0; i < n; i++) {
            int value = scanner.nextInt();
            counts[value]++;
        }

        int[] result = new int[n];
        int index = 0;

        // Восстанавливаем отсортированный массив по накопленным количествам.
        for (int value = 0; value <= MAX_VALUE; value++) {
            while (counts[value] > 0) {
                result[index++] = value;
                counts[value]--;
            }
        }
        return result;
    }
}
