package by.it.group551004.pedko.lesson05;

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

    void swap(int[] dataArray, int i, int j)
    {
        if (i != j) {
            dataArray[i] = dataArray[i] + dataArray[j];
            dataArray[j] = dataArray[i] - dataArray[j];
            dataArray[i] = dataArray[i] - dataArray[j];
        }
    }

    int partition(int[] dataArray, final int arrayLength, int low, int high)
    {
        int pivot, randomIndex, i, j;
        boolean isDoNotStop;

        randomIndex = 0;
        pivot = 0;
        i = low;
        j = high;
        isDoNotStop = true;

        randomIndex = low + (int)(Math.random() * (high - low + 1));
        pivot = dataArray[randomIndex];

        while (isDoNotStop)
        {
            while (i <= high && dataArray[i] < pivot)
                i++;

            while (j >= low && dataArray[j] > pivot)
                j--;

            if (i >= j)
                isDoNotStop = false;
            else
                swap(dataArray, i++, j--);
        }

        return j;
    }

    void quicksort(int[] dataArray, int low, int high)
    {
        int point;
        int arrayLength;
        point = 0;
        arrayLength = dataArray.length;

        if (dataArray == null)
            System.out.print("Массив пуст, сначала введите массив\n");
        else
            if (low < high)
            {
                point = partition(dataArray, arrayLength, low, high);
                quicksort(dataArray, low, point);
                quicksort(dataArray, point + 1, high);
            }
    }

    public int binaryFindLowerBound(int[] arr, int target) {
        //Поиск последнего индекса элемента, после которого target будет больше за элемент
        int left;
        int right;
        int middle;

        left = 0;
        right = arr.length;
        middle = 0;

        while (left < right) {
            middle = (left + right) / 2;

            if (arr[middle] < target)
                left = middle + 1;
            else
                right = middle;
        }
        return left;
    }

    public int binaryFindUpperBound(int[] arr, int target) {
        //Поиск последнего индекса элемента, после которого target будет больше за элемент
        int left;
        int right;
        int middle;

        left = 0;
        right = arr.length;
        middle = 0;

        while (left < right) {
            middle = (left + right) / 2;

            if (arr[middle] <= target)
                left = middle + 1;
            else
                right = middle;
        }
        return left;
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
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        int[] starts;
        int[] stops;
        int startsCount;
        int endsCount;

        endsCount = 0;
        startsCount = 0;

        starts = new int[segments.length];
        stops = new int[segments.length];

        for (int i = 0; i < segments.length; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        quicksort(starts, 0, starts.length - 1);
        quicksort(stops, 0, stops.length - 1);

        for (int i = 0; i < m; i++) {
            startsCount = binaryFindUpperBound(starts, points[i]); // количество starts ≤ x
            endsCount   = binaryFindLowerBound(stops, points[i]);   // количество ends < x
            result[i] = startsCount - endsCount;
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков

            return 0;
        }
    }

}
