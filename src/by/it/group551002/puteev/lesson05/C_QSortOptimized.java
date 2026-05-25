package by.it.group551002.puteev.lesson05;

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
        // Сортировка на месте с оптимизациями
        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            // Бинарный поиск правого края (первый сегмент, где start > p)
            int rightBound = findRightBound(segments, p);

            int count = 0;
            // Проверяем все сегменты от 0 до rightBound
            for (int j = 0; j < rightBound; j++) {
                if (segments[j].start <= p && segments[j].stop >= p) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    private void quickSort(Segment[] arr, int left, int right) {
        while (left < right) {
            // 3-way partitioning
            int lt = left, gt = right;
            Segment pivot = arr[left];
            int i = left + 1;

            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) swap(arr, lt++, i++);
                else if (cmp > 0) swap(arr, i, gt--);
                else i++;
            }

            // Элиминация хвостовой рекурсии: выбираем меньшую часть для рекурсии
            if (lt - left < right - gt) {
                quickSort(arr, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(arr, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int findRightBound(Segment[] arr, int p) {
        int low = 0, high = arr.length;
        while (low < high) {
            int mid = (low + high) / 2;
            if (arr[mid].start <= p) low = mid + 1;
            else high = mid;
        }
        return low; // Индекс первого элемента, у которого start > p
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Корректируем границы, если они пришли в обратном порядке
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            // Сортировка по началу, затем по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

}
