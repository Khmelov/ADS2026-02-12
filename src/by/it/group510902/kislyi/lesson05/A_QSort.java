package by.it.group510902.kislyi.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
*/

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            segments[i] = new Segment(Math.min(start, end), Math.max(start, end));
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по началу
        quickSort(segments, 0, segments.length - 1);

        // Создаем массив начал и концов для бинарного поиска
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Сортируем концы отдельно для бинарного поиска
        int[] endsSorted = ends.clone();
        quickSortInt(endsSorted, 0, endsSorted.length - 1);

        // Для каждой точки считаем количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Находим количество отрезков, которые начались до или в точке
            int countStart = countLessOrEqual(starts, point);

            // Находим количество отрезков, которые закончились до точки (не покрывают)
            int countEndBefore = countLessThan(endsSorted, point);

            // Покрывают те, которые начались до/в точке и закончились после/в точке
            result[i] = countStart - countEndBefore;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка для отрезков
    private void quickSort(Segment[] arr, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(arr, left, right);
            quickSort(arr, left, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, right);
        }
    }

    private int partition(Segment[] arr, int left, int right) {
        Segment pivot = arr[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                i++;
                Segment temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        Segment temp = arr[i + 1];
        arr[i + 1] = arr[right];
        arr[right] = temp;

        return i + 1;
    }

    // Быстрая сортировка для int
    private void quickSortInt(int[] arr, int left, int right) {
        if (left < right) {
            int pivotIndex = partitionInt(arr, left, right);
            quickSortInt(arr, left, pivotIndex - 1);
            quickSortInt(arr, pivotIndex + 1, right);
        }
    }

    private int partitionInt(int[] arr, int left, int right) {
        int pivot = arr[right];
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[right];
        arr[right] = temp;

        return i + 1;
    }

    // Бинарный поиск: количество элементов <= target
    private int countLessOrEqual(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        int result = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= target) {
                result = mid + 1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    // Бинарный поиск: количество элементов < target
    private int countLessThan(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        int result = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < target) {
                result = mid + 1;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}


