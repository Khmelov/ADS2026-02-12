package by.it.group551004.pedko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
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

    void quicksort(int[] dataArray, int low, int high) {
        if (dataArray == null) {
            System.out.print("Массив пуст, сначала введите массив\n");
            return;
        }
        if (low >= high)
            return;

        int pivot = dataArray[low + (int)(Math.random() * (high - low + 1))],
                i = low,
                j = low,    //current element
                k = high;

        while (j <= k) {
            if (dataArray[j] < pivot)
                swap(dataArray, i++, j++);
            else
                if (dataArray[j] > pivot)
                    swap(dataArray, j, k--);  // Кидаем в самый конец
                else
                    ++j;
        }

        quicksort(dataArray, low, i);
        quicksort(dataArray, j, high);
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

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
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
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            return 0;
        }
    }

}
