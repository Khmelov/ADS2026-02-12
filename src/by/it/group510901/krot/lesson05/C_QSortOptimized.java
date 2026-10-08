package by.it.group510901.krot.lesson05;

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
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // читаем отрезки
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // СОРТИРОВКА: быстрая сортировка с 3-разбиением
        quickSort3Way(segments, 0, n - 1);

        // ПОИСК: для каждой точки бинарным поиском
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // находим левую границу: первый отрезок, у которого stop >= point
            int left = findLeftBound(segments, point);

            // находим правую границу: последний отрезок, у которого start <= point
            int right = findRightBound(segments, point);

            // если границы корректны, считаем количество
            if (left <= right && left != -1 && right != -1) {
                result[i] = right - left + 1;
            } else {
                result[i] = 0;
            }
        }

        return result;
    }

    // ============ БЫСТРАЯ СОРТИРОВКА С 3-РАЗБИЕНИЕМ ============

    private void quickSort3Way(Segment[] arr, int low, int high) {
        if (low >= high) return;

        // опорный элемент — первый в отрезке
        Segment pivot = arr[low];

        // три указателя
        int lt = low;      // граница "меньших"
        int i = low + 1;   // текущий проверяемый
        int gt = high;     // граница "больших"

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);

            if (cmp < 0) {
                // arr[i] < pivot — меняем с lt и сдвигаем оба
                swap(arr, i, lt);
                i++;
                lt++;
            } else if (cmp > 0) {
                // arr[i] > pivot — меняем с gt
                swap(arr, i, gt);
                gt--;
            } else {
                // arr[i] == pivot — просто идём дальше
                i++;
            }
        }

        // рекурсивно сортируем левую и правую части
        // средняя (равные pivot) уже на месте
        quickSort3Way(arr, low, lt - 1);
        quickSort3Way(arr, gt + 1, high);
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // ============ БИНАРНЫЙ ПОИСК ============

    // ищем первый индекс, где stop >= point
    private int findLeftBound(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].stop >= point) {
                result = mid;    // запомнили, но ищем левее
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return result;
    }

    // ищем последний индекс, где start <= point
    private int findRightBound(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].start <= point) {
                result = mid;    // запомнили, но ищем правее
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    // ============ КЛАСС ОТРЕЗОК ============

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // если концы перепутаны, меняем местами
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
            // сначала сравниваем по start
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            // если start равны, сравниваем по stop
            return Integer.compare(this.stop, o.stop);
        }
    }
}