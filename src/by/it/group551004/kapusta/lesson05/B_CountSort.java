package by.it.group551004.kapusta.lesson05;

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

        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = scanner.nextInt();
        int[] points = new int[n];

        // читаем массив
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // сортировка подсчётом
        int maxValue = 10; // по условию числа ≤ 10
        int[] count = new int[maxValue + 1];

        // считаем, сколько раз встречается каждое число
        for (int x : points) {
            count[x]++;
        }

        // восстанавливаем отсортированный массив
        int index = 0;
        for (int value = 0; value <= maxValue; value++) {
            for (int c = 0; c < count[value]; c++) {
                points[index++] = value;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }
}
