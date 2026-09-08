package by.it.group551002.kuzmenia.lesson05;

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

    // быстрая сортировка
    private void quickSort(int[] array, int left, int right)
    {
        if (left >= right) return;
        int pivot = array[left + (right - left) / 2];
        int i = left, j = right;
        while (i <= j)
        {
            while (array[i] < pivot) i++;
            while (array[j] > pivot) j--;
            if (i <= j)
            {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        quickSort(array, left, j);
        quickSort(array, i, right);
    }

    // бинарный поиск кол-ва эл-тов <= value
    private int countLessOrEqual(int[] arr, int value)
    {
        int left = 0, right = arr.length - 1;
        int count = 0;
        while (left <= right)
        {
            int mid = (left + right) / 2;
            if (arr[mid] <= value)
            {
                count = mid + 1;
                left = mid + 1;
            }
            else
            {
                right = mid - 1;
            }
        }
        return count;
    }

    // бинарный поиск кол-ва эл-тов < value
    private int countLess(int[] arr, int value) {
        int left = 0, right = arr.length - 1;
        int count = 0;
        while (left <= right)
        {
            int mid = (left + right) / 2;
            if (arr[mid] < value)
            {
                count = mid + 1;
                left = mid + 1;
            }
            else
            {
                right = mid - 1;
            }
        }
        return count;
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt(); // отрезки
        int m = scanner.nextInt(); // точки

        int[] starts = new int[n]; // начала отрезков
        int[] ends = new int[n];   // концы отрезков

        // чтение отрезков
        for (int i = 0; i < n; i++)
        {
            int a = scanner.nextInt();
            int b = scanner.nextInt();

            starts[i] = Math.min(a, b);
            ends[i] = Math.max(a, b);
        }

        int[] points = new int[m];

        // чтение точек
        for (int i = 0; i < m; i++)
        {
            points[i] = scanner.nextInt();
        }

        quickSort(starts, 0, n - 1);
        quickSort(ends, 0, n-1);

        int[] result = new int[m];

        // для каждой точки считаем вхождения
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // сколько отрезков началось до точки (включительно)
            int started = countLessOrEqual(starts, point);

            // сколько отрезков закончилось строго до точки
            int finished = countLess(ends, point);

            result[i] = started - finished;
        }

        return result;
    }

    //отрезок (не использовал)
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop)
        {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o)
        {
            return Integer.compare(this.start, o.start);
        }
    }

}
