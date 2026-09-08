package by.it.group551002.kubarko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

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

        int[] starts = new int[n];
        int[] stops = new int[n];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        // Разделение отрезков
        for (int i = 0; i < n; i++)
        {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        quickSort(starts, 0, starts.length - 1);
        quickSort(stops, 0, stops.length - 1);
        int count = 0;
        for(int i = 0; i < m; i++)
        {
            int low = 0, high = starts.length;
            while (low < high)
            {
                int mid = (low + high) / 2;
                if (starts[mid] <= points[i]) low = mid + 1;
                else high = mid;
            }
            int start = low;

            low = 0;
            high = stops.length;
            while (low < high)
            {
                int mid = (low + high) / 2;
                if (stops[mid] < points[i]) low = mid + 1;
                else high = mid;
            }
            int finish = low;

            result[i] = start-finish;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j <= high - 1; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static void quickSort(int[] arr, int low, int high) {
        if (low < high) {

            int m = partition(arr, low, high);

            quickSort(arr, low, m - 1);
            quickSort(arr, m + 1, high);
        }
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if(start <= stop)
            {
                this.start = start;
                this.stop = stop;
            }
            else
            {
                this.start = stop;
                this.stop = start;
            }

        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков

            return 0;
        }
    }

}
