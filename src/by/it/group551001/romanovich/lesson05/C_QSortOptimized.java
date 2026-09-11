package by.it.group551001.romanovich.lesson05;

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
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = segments[i].stop;
        }
        quickSort(segments, 0, n - 1);
        quickSort(stops, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int started = countStartedBeforeOrAt(segments, points[i]);
            int stopped = countStoppedBefore(stops, points[i]);
            result[i] = started - stopped;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            int byStart = Integer.compare(start, o.start);
            return byStart != 0 ? byStart : Integer.compare(stop, o.stop);
        }
    }

    private void quickSort(Segment[] array, int left, int right) {
        while (left < right) {
            int less = left;
            int equal = left;
            int greater = right;
            Segment pivot = array[left + (right - left) / 2];

            while (equal <= greater) {
                int compare = array[equal].compareTo(pivot);
                if (compare < 0) {
                    swap(array, less++, equal++);
                } else if (compare > 0) {
                    swap(array, equal, greater--);
                } else {
                    equal++;
                }
            }

            if (less - left < right - greater) {
                quickSort(array, left, less - 1);
                left = greater + 1;
            } else {
                quickSort(array, greater + 1, right);
                right = less - 1;
            }
        }
    }

    private void quickSort(int[] array, int left, int right) {
        while (left < right) {
            int less = left;
            int equal = left;
            int greater = right;
            int pivot = array[left + (right - left) / 2];

            while (equal <= greater) {
                if (array[equal] < pivot) {
                    swap(array, less++, equal++);
                } else if (array[equal] > pivot) {
                    swap(array, equal, greater--);
                } else {
                    equal++;
                }
            }

            if (less - left < right - greater) {
                quickSort(array, left, less - 1);
                left = greater + 1;
            } else {
                quickSort(array, greater + 1, right);
                right = less - 1;
            }
        }
    }

    private void swap(Segment[] array, int first, int second) {
        Segment temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }

    private void swap(int[] array, int first, int second) {
        int temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }

    private int countStartedBeforeOrAt(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (segments[middle].start <= point) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }

    private int countStoppedBefore(int[] stops, int point) {
        int left = 0;
        int right = stops.length;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (stops[middle] < point) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }

}
