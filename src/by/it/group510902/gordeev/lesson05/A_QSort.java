package by.it.group510902.gordeev.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    // Основной метод: для каждой точки считает, скольким отрезкам она принадлежит
    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();          // количество отрезков
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();          // количество точек
        int[] points = new int[m];
        int[] result = new int[m];

        // Читаем отрезки
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        // Читаем точки
    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Вытаскиваем все начала и все концы в отдельные массивы
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Сортируем для бинарного поиска
        Arrays.sort(starts);
        Arrays.sort(ends);

        // Для каждой точки: сколько отрезков началось до/в ней минус сколько закончилось до неё
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int countStart = upperBound(starts, p);   // кол-во start <= p
            int countEnd = lowerBound(ends, p);       // кол-во end < p
        Arrays.sort(starts);
        Arrays.sort(ends);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int countStart = upperBound(starts, p);
            int countEnd = lowerBound(ends, p);
            result[i] = countStart - countEnd;
        }

        return result;
    }

    // Бинарный поиск: возвращает кол-во элементов <= key (первый индекс, где arr[index] > key)
    private int upperBound(int[] arr, int key) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= key) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    // Бинарный поиск: возвращает кол-во элементов < key (первый индекс, где arr[index] >= key)
    private int lowerBound(int[] arr, int key) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < key) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    // Класс-отрезок: start - начало, stop - конец (start <= stop)
    private class Segment {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }
    }
}