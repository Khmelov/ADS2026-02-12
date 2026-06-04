package by.it.group551003.kernitsky.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.PriorityQueue;
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

        // 1. Сортировка отрезков по началу (in-place) с 3-разбиением и устранением хвостовой рекурсии
        quickSort3Way(segments, 0, n - 1);

        // 2. Подготовка точек с сохранением исходных индексов и сортировка
        PointIndex[] pts = new PointIndex[m];
        for (int i = 0; i < m; i++) {
            pts[i] = new PointIndex(points[i], i);
        }
        quickSort3WayPoints(pts, 0, m - 1);

        // 3. Обработка точек с помощью очереди с приоритетом и бинарного поиска
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int segIdx = 0; // индекс первого ещё не добавленного отрезка

        for (PointIndex pt : pts) {
            int x = pt.value;

            // Бинарный поиск индекса первого отрезка с началом > x
            int pos = upperBound(segments, segIdx, n - 1, x);

            // Добавляем все отрезки, которые начались не позже x
            while (segIdx < pos) {
                minHeap.offer(segments[segIdx].stop);
                segIdx++;
            }

            // Удаляем отрезки, закончившиеся раньше x
            while (!minHeap.isEmpty() && minHeap.peek() < x) {
                minHeap.poll();
            }

            result[pt.index] = minHeap.size();
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // ========== Вспомогательные методы ==========

    // Бинарный поиск первого элемента строго больше x (upper bound) на отрезке [left, right]
    private int upperBound(Segment[] arr, int left, int right, int x) {
        int l = left, r = right, ans = right + 1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (arr[mid].start > x) {
                ans = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return ans;
    }

    // 3-разбиение для Segment[]
    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] piv = partition3Segment(arr, low, high);
            // piv[0] – индекс последнего элемента меньше опорного,
            // piv[1] – индекс первого элемента больше опорного
            // сортируем меньший подмассив рекурсивно, больший – в цикле
            if (piv[0] - low < high - piv[1]) {
                quickSort3Way(arr, low, piv[0]);
                low = piv[1];
            } else {
                quickSort3Way(arr, piv[1], high);
                high = piv[0];
            }
        }
    }

    private int[] partition3Segment(Segment[] arr, int low, int high) {
        Segment pivot = arr[high];
        int lt = low, i = low, gt = high;
        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, lt++, i++);
            } else if (cmp > 0) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt - 1, gt + 1};
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // 3-разбиение для PointIndex[]
    private void quickSort3WayPoints(PointIndex[] arr, int low, int high) {
        while (low < high) {
            int[] piv = partition3Point(arr, low, high);
            if (piv[0] - low < high - piv[1]) {
                quickSort3WayPoints(arr, low, piv[0]);
                low = piv[1];
            } else {
                quickSort3WayPoints(arr, piv[1], high);
                high = piv[0];
            }
        }
    }

    private int[] partition3Point(PointIndex[] arr, int low, int high) {
        PointIndex pivot = arr[high];
        int lt = low, i = low, gt = high;
        while (i <= gt) {
            if (arr[i].value < pivot.value) {
                swapPoint(arr, lt++, i++);
            } else if (arr[i].value > pivot.value) {
                swapPoint(arr, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt - 1, gt + 1};
    }

    private void swapPoint(PointIndex[] arr, int i, int j) {
        PointIndex tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // Класс для хранения точки с исходным индексом
    private static class PointIndex {
        int value;
        int index;

        PointIndex(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // на случай перепутанных концов
            if (start > stop) {
                int tmp = start;
                start = stop;
                stop = tmp;
            }
            this.start = start;
            this.stop = stop;
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