package by.it.group551004.fedkovich.lesson05;

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

    void quicksort(Segment[] segments, int left, int right) {
        while (left < right) {
            int pivotIdx = left;

            int[] partitions = partition(segments, left, right, pivotIdx);
            int leftEnd = partitions[0];
            int rightStart = partitions[1];

            if (leftEnd - left < rightStart - right) {
                quicksort(segments, left, leftEnd);
                left = rightStart;
            } else {
                quicksort(segments, rightStart, right);
                right = leftEnd;
            }
        }
    }

    int[] partition(Segment[] segments, int left, int right, int pivotIdx) {
        int lt = left;
        int eq = left;
        int gt = right;

        Segment pivot = segments[pivotIdx];

        while (eq <= gt) {
            int cmp = segments[eq].compareTo(pivot);
            if (cmp == 0) {
                ++eq;
            } else if (cmp > 0) {
                swap(segments, eq, gt);
                --gt;
            } else {
                swap(segments, eq, lt);
                ++lt;
                ++eq;
            }
        }

        return new int[] { lt - 1, gt + 1 };
    }

    void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    int binarySearchFirstGreater(Segment[] segments, int target) {
        int left = 0;
        int right = segments.length;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].start <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    int countCovers(Segment[] segments, int point) {
        int lastIdx = binarySearchFirstGreater(segments, point);
        int count = 0;

        for (int i = 0; i < lastIdx; ++i) {
            if (segments[i].stop >= point) {
                ++count;
            }
        }

        return count;
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        // !!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        // число отрезков отсортированного массива
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        // число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // читаем сами отрезки
        for (int i = 0; i < n; i++) {
            // читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        // тут реализуйте логику задачи с применением быстрой сортировки
        // в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        quicksort(segments, 0, n - 1);
        for (int i = 0; i < m; i++) {
            result[i] = countCovers(segments, points[i]);
        }

        // !!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            return Integer.compare(this.start, other.start);
        }
    }

}
