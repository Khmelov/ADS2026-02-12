package by.it.group510901.jalilova.lesson05;

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
        if (stream != null) {
            int[] result = instance.getAccessory2(stream);
            for (int index : result) {
                System.out.print(index + " ");
            }
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        int[] starts = new int[n];
        int[] stops = new int[n];

        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            starts[i] = Math.min(a, b);
            stops[i] = Math.max(a, b);
        }

        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Оптимизированная сортировка на месте
        qSort(starts, 0, n - 1);
        qSort(stops, 0, n - 1);

        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            // Бинарный поиск: сколько начали работу до точки (включительно)
            int s = binarySearchCount(starts, points[i], true);
            // Бинарный поиск: сколько закончили работу строго до точки
            int e = binarySearchCount(stops, points[i], false);
            result[i] = s - e;
        }

        return result;
    }

    // QuickSort с 3-разбиением и элиминацией хвостовой рекурсии
    private void qSort(int[] a, int left, int right) {
        while (left < right) {
            int[] m = partition3(a, left, right);
            // Чтобы минимизировать глубину стека, идем в меньшую часть рекурсивно,
            // а большую обрабатываем циклом (элиминация хвоста)
            if (m[0] - left < right - m[1]) {
                qSort(a, left, m[0] - 1);
                left = m[1] + 1;
            } else {
                qSort(a, m[1] + 1, right);
                right = m[0] - 1;
            }
        }
    }

    // 3-разбиение (Dutch National Flag partition)
    private int[] partition3(int[] a, int left, int right) {
        int pivot = a[left];
        int lt = left;  // конец части < pivot
        int i = left;   // текущий элемент
        int gt = right; // начало части > pivot
        while (i <= gt) {
            if (a[i] < pivot) swap(a, lt++, i++);
            else if (a[i] > pivot) swap(a, i, gt--);
            else i++;
        }
        return new int[]{lt, gt};
    }

    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // Бинарный поиск количества элементов
    private int binarySearchCount(int[] a, int x, boolean inclusive) {
        int left = 0, right = a.length - 1;
        int res = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (inclusive) { // Ищем последний индекс элемента <= x
                if (a[mid] <= x) {
                    res = mid;
                    left = mid + 1;
                } else right = mid - 1;
            } else { // Ищем последний индекс элемента < x
                if (a[mid] < x) {
                    res = mid;
                    left = mid + 1;
                } else right = mid - 1;
            }
        }
        return res + 1;
    }

    private class Segment implements Comparable<Object> {
        int start;
        int stop;
        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }
        @Override
        public int compareTo(Object o) { return 0; }
    }
}