package by.it.group551002.kurzhalov.lesson05;

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
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        quickSort(starts, 0, n - 1);
        quickSort(stops, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int started = binarySearchLastLessOrEqual(starts, points[i]);
            int ended = binarySearchLastLess(stops, points[i]);
            result[i] = started - ended;
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort(int[] a, int left, int right) {
        while (left < right) {
            int lt = left;
            int gt = right;
            int pivot = a[left + (right - left) / 2];
            int i = left;

            while (i <= gt) {
                if (a[i] < pivot) {
                    swap(a, lt++, i++);
                } else if (a[i] > pivot) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }
            if ((lt - left) < (right - gt)) {
                quickSort(a, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(a, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private int binarySearchLastLessOrEqual(int[] a, int x) {
        int left = 0, right = a.length - 1;
        int index = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (a[mid] <= x) {
                index = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return index + 1;
    }

    private int binarySearchLastLess(int[] a, int x) {
        int left = 0, right = a.length - 1;
        int index = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (a[mid] < x) {
                index = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return index + 1;
    }

    //отрезок
    private class Segment implements Comparable<Segment>{
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
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
