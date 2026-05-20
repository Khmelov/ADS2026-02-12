package by.it.group510901.gulchenko.lesson05;

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

        quickSort(segments, 0, segments.length - 1);

        int[] starts = new int[n];
        int[] stops = new int[n];

        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        quickSort(starts, 0, starts.length - 1);
        quickSort(stops, 0, stops.length - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];

            int startedSegments = countLessOrEqual(starts, point);
            int finishedSegments = countLess(stops, point);

            result[i] = startedSegments - finishedSegments;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort(Segment[] array, int left, int right) {
        while (left < right) {
            int[] borders = partition(array, left, right);

            if (borders[0] - left < right - borders[1]) {
                quickSort(array, left, borders[0] - 1);
                left = borders[1] + 1;
            } else {
                quickSort(array, borders[1] + 1, right);
                right = borders[0] - 1;
            }
        }
    }

    private int[] partition(Segment[] array, int left, int right) {
        Segment pivot = array[left + (right - left) / 2];

        int less = left;
        int current = left;
        int greater = right;

        while (current <= greater) {
            int compareResult = array[current].compareTo(pivot);

            if (compareResult < 0) {
                swap(array, less, current);
                less++;
                current++;
            } else if (compareResult > 0) {
                swap(array, current, greater);
                greater--;
            } else {
                current++;
            }
        }

        return new int[]{less, greater};
    }

    private void swap(Segment[] array, int firstIndex, int secondIndex) {
        Segment temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    private void quickSort(int[] array, int left, int right) {
        while (left < right) {
            int[] borders = partition(array, left, right);

            if (borders[0] - left < right - borders[1]) {
                quickSort(array, left, borders[0] - 1);
                left = borders[1] + 1;
            } else {
                quickSort(array, borders[1] + 1, right);
                right = borders[0] - 1;
            }
        }
    }

    private int[] partition(int[] array, int left, int right) {
        int pivot = array[left + (right - left) / 2];

        int less = left;
        int current = left;
        int greater = right;

        while (current <= greater) {
            if (array[current] < pivot) {
                swap(array, less, current);
                less++;
                current++;
            } else if (array[current] > pivot) {
                swap(array, current, greater);
                greater--;
            } else {
                current++;
            }
        }

        return new int[]{less, greater};
    }

    private void swap(int[] array, int firstIndex, int secondIndex) {
        int temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
    }

    private int countLessOrEqual(int[] array, int value) {
        int left = 0;
        int right = array.length;

        while (left < right) {
            int middle = left + (right - left) / 2;

            if (array[middle] <= value) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }

    private int countLess(int[] array, int value) {
        int left = 0;
        int right = array.length;

        while (left < right) {
            int middle = left + (right - left) / 2;

            if (array[middle] < value) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;

            if (this.start > this.stop) {
                int temp = this.start;
                this.start = this.stop;
                this.stop = temp;
            }
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            int result = Integer.compare(this.start, o.start);

            if (result == 0) {
                result = Integer.compare(this.stop, o.stop);
            }

            return result;
        }
    }

}