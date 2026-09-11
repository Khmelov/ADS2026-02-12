package by.it.group551003.tereshko.lesson05;

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
        quickSort3(segments, 0, segments.length);
        for (int i = 0; i < points.length; i++) {
            result[i] = binarySearch(segments, points[i], 0, segments.length);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int binarySearch(Segment[] arr, int req, int left, int right) {
        if (left >= right)
            return 0;
        int mid = left + (right - left) / 2;
        int counter = 0;
        if (arr[mid].start <= req) {
            while (mid > 0 && arr[mid - 1].start <= req)
                mid--;
            while (mid < arr.length && arr[mid].start <= req) {
                if (arr[mid].start <= req && arr[mid].stop >= req)
                    counter++;
                mid++;
            }
            return counter;
        }
        if (arr[mid].start > req)
            return binarySearch(arr, req, left, mid);
        else
            return binarySearch(arr, req, mid + 1, right);
    }

    private static class twoIndex {
        int lt, gt;

        twoIndex(int lt, int gt) {
            this.lt = lt;
            this.gt = gt;
        }
    }

    void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    twoIndex partition3(Segment[] arr, int left, int right) {
        int lt = left, eq = left + 1, gt = right;
        while (eq < gt) {
            if (arr[eq].compareTo(arr[left]) < 0)
                swap(arr, lt++, eq++);
            if (arr[eq].compareTo(arr[left]) > 0)
                swap(arr, --gt, eq);
            if (arr[eq].compareTo(arr[left]) == 0)
                eq++;
        }
        return new twoIndex(lt, gt);
    }

    void quickSort3(Segment[] arr, int left, int right) {
        while (left < right) {
            twoIndex eqZone = partition3(arr, left, right);
            if (eqZone.lt - left < right - eqZone.gt) {
                quickSort3(arr, left, eqZone.lt);
                left = eqZone.gt;
            } else {
                quickSort3(arr, eqZone.gt, right);
                right = eqZone.lt;
            }
        }
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
            return this.start - ((Segment) o).start;
        }
    }

}
