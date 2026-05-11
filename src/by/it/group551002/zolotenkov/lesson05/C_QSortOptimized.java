package by.it.group551002.zolotenkov.lesson05;

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
        // Сортируем отрезки на месте (3-разбиение, элиминация хвостовой рекурсии)
        quickSortSegments(segments, 0, n - 1);

        // Формируем массив концов и сортируем его тем же алгоритмом
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = segments[i].stop;
        }
        quickSortInts(stops, 0, n - 1);

        // Для каждой точки бинарным поиском находим первый отрезок, не подходящий по start,
        // и первый отрезок, подходящий по stop, разность даёт количество покрывающих камер
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int countStartsLeq = upperBoundStarts(segments, p); // start <= p
            int countStopsLess = lowerBoundStops(stops, p);     // stop < p
            result[i] = countStartsLeq - countStopsLess;
        }

        scanner.close();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // всегда start <= stop
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }
    // ===== 3-разбиение для отрезков (с элиминацией хвостовой рекурсии) =====
    private void quickSortSegments(Segment[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = arr[low];
            int i = low + 1;
            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) {
                    swapSegments(arr, lt++, i++);
                } else if (cmp > 0) {
                    swapSegments(arr, i, gt--);
                } else {
                    i++;
                }
            }
            // lt .. gt – равные pivot
            // Рекурсивно сортируем меньшую часть, большую – в цикле
            if (lt - low < high - gt) {
                quickSortSegments(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSortSegments(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private void swapSegments(Segment[] arr, int i, int j) {
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // ===== 3-разбиение для int[] (для сортировки концов) =====
    private void quickSortInts(int[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            int pivot = arr[low];
            int i = low + 1;
            while (i <= gt) {
                if (arr[i] < pivot) {
                    swapInts(arr, lt++, i++);
                } else if (arr[i] > pivot) {
                    swapInts(arr, i, gt--);
                } else {
                    i++;
                }
            }
            if (lt - low < high - gt) {
                quickSortInts(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSortInts(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private void swapInts(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // ===== Бинарный поиск «первого отрезка решения» =====
    // Первый индекс, где start > point (количество start <= point = возвращаемый индекс)
    private int upperBoundStarts(Segment[] segments, int point) {
        int lo = 0, hi = segments.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (segments[mid].start <= point) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo; // lo == количество start <= point
    }

    // Первый индекс, где stop >= point (количество stop < point = возвращаемый индекс)
    private int lowerBoundStops(int[] stops, int point) {
        int lo = 0, hi = stops.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (stops[mid] < point) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo; // lo == количество stop < point
    }
}
