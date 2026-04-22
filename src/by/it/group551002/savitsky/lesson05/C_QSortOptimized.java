package by.it.group551002.savitsky.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
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

    void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    int[] partition3(Segment[] arr, int left, int right, Comparator<Segment> cmp) {

        Segment pivot = arr[(left + right) / 2];
        int lt = left;
        int i = left + 1;
        int gt = right;

        while (i <= gt) {
            int cmpResult = cmp.compare(arr[i], pivot);

            if (cmpResult < 0) {
                swap(arr, lt, i);
                lt++;
                i++;
            } else if (cmpResult > 0) {
                swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    void quicksort(Segment[] arr, int left, int right, Comparator<Segment> cmp) {
        if (left < right) {

            int[] bounds = partition3(arr, left, right, cmp);
            int lt = bounds[0];
            int gt = bounds[1];

            while (left < right) {
                quicksort(arr, left, lt - 1, cmp);
                left = gt + 1;
            }
        }
    }

    private int upperBound(Segment[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right){
            int mid = (left + right) / 2;

            int value = arr[mid].start;

            if (value <= target){
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    private int lowerBound(Segment[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right){
            int mid = (left + right) / 2;

            int value = arr[mid].stop;

            if (value < target){
                left = mid + 1;
            } else {
                right = mid;
            }
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
        quicksort(segments, 0, n - 1, Comparator.comparingInt(a -> a.start));

        Segment[] byStop = Arrays.copyOf(segments, n);
        quicksort(byStop, 0, n - 1, Comparator.comparingInt(a -> a.stop));

        for (int i = 0; i < points.length; ++i) {

            int countStart = upperBound(segments, points[i]);
            int countStop = lowerBound(byStop, points[i]);

            result[i] = countStart - countStop;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            return 0;
        }
    }

}