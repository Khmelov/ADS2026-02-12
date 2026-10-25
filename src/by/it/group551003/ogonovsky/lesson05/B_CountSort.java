package by.it.group551003.ogonovsky.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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

        // значения от 1 до 10, заводим счётчики на индексы 0..10
        int[] count = new int[11];

        // 1. подсчёт количества вхождений каждого значения - O(n)
        for (int i = 0; i < n; i++) {
            count[points[i]]++;
        }

        // 2. восстановление отсортированного массива - O(n + k)
        int idx = 0;
        for (int v = 1; v <= 10; v++) {
            for (int j = 0; j < count[v]; j++) {
                points[idx++] = v;
            }
        }

        return points;
    }
}