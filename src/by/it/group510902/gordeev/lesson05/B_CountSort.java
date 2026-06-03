package by.it.group510902.gordeev.lesson05;

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

    // Сортировка подсчётом (Counting Sort) для чисел от 0 до 10
    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();      // количество чисел
        int[] points = new int[n];

        // Читаем исходный массив
    int[] countSort(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] points = new int[n];

        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Массив для подсчёта: индексы от 0 до 10 (значения чисел)
        // По условию задачи числа в диапазоне [0, 10]
        int[] count = new int[11];

        // Подсчитываем, сколько раз встречается каждое число
        int[] count = new int[11];

        for (int value : points) {
            count[value]++;
        }

        // Восстанавливаем отсортированный массив
        int index = 0;
        for (int value = 0; value <= 10; value++) {      // идём по значениям от меньшего к большему
            for (int j = 0; j < count[value]; j++) {     // каждое значение повторяется count[value] раз
                points[index++] = value;                 // записываем в исходный массив по порядку
            }
        }

        return points;   // возвращаем отсортированный массив
        int index = 0;
        for (int value = 0; value <= 10; value++) {
            for (int j = 0; j < count[value]; j++) {
                points[index++] = value;
            }
        }

        return points;
    }
}