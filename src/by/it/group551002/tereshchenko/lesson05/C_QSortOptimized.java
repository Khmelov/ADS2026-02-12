package by.it.group551002.tereshchenko.lesson05;

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
        int[] stops = new int[n];
        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
            stops[i] = segments[i].stop;
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        quickSortSegments(segments, 0, n - 1);
        quickSortInt(stops, 0, n - 1);
        for (int i = 0; i < m; i++) {
            int point = points[i];

            int started = countStartsLessOrEqual(segments, point);
            int finished = countStopsLess(stops, point);

            result[i] = started - finished;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    private int countStartsLessOrEqual(Segment[] segments, int point) {
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
    int countStopsLess(int[] stops, int point) {
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
    void swapSegments(Segment[] array, int i, int j) {
        Segment temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    void quickSortSegments(Segment[] array, int left, int right) {
        while (left < right) {
            int l = left;
            int i = left + 1;
            int r = right;

            Segment pivot = array[left];

            while (i <= r) {
                int cmp = array[i].compareTo(pivot);

                if (cmp < 0) {
                    swapSegments(array, l, i);
                    l++;
                    i++;
                } else if (cmp > 0) {
                    swapSegments(array, i, r);
                    r--;
                } else {
                    i++;
                }
            }

            if (l - left < right - r) {
                quickSortSegments(array, left, l - 1);
                left = r + 1;
            } else {
                quickSortSegments(array, r + 1, right);
                right = l - 1;
            }
        }
    }

    void swapInt(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    void quickSortInt(int[] array, int left, int right) {
        while (left < right) {
            int l = left;
            int i = left + 1;
            int r = right;

            int pivot = array[left];

            while (i <= r) {
                if (array[i] < pivot) {
                    swapInt(array, l, i);
                    l++;
                    i++;
                } else if (array[i] > pivot) {
                    swapInt(array, i, r);
                    r--;
                } else {
                    i++;
                }
            }

            if (l - left < right - r) {
                quickSortInt(array, left, l - 1);
                left = r + 1;
            } else {
                quickSortInt(array, r + 1, right);
                right = l - 1;
            }
        }
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            Segment other = (Segment) o;
            if (this.start < other.start) {
                return -1;
            }
            if (this.start > other.start) {
                return 1;
            }
            if (this.stop < other.stop) {
                return -1;
            }
            if (this.stop > other.stop) {
                return 1;
            }
            return 0;
        }
    }

}
