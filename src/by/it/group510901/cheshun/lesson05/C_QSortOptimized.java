package by.it.group510901.cheshun.lesson05;

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
    // быстрая сортировка с 3-разбиением (оптимизация памяти - на месте)
    private void quickSort(Segment[] arr, int low, int high) {
        if (low >= high) return;

        // элиминация хвостовой рекурсии через цикл
        while (low < high) {
            Segment pivot = arr[low];
            int lt = low;
            int gt = high;
            int i = low + 1;

            // 3-разбиение: < pivot, = pivot, > pivot
            while (i <= gt) {
                if (arr[i].compareTo(pivot) < 0) {
                    swap(arr, lt++, i++);
                } else if (arr[i].compareTo(pivot) > 0) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }

            // рекурсивно сортируем меньшую часть, большую - через цикл
            if (lt - low < high - gt) {
                quickSort(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    // бинарный поиск первого отрезка, где start <= point
    private int binarySearchFirst(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
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
// сортируем отрезки на месте (оптимизация памяти)
        quickSort(segments, 0, n - 1);

// для каждой точки ищем подходящие отрезки
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // бинарный поиск первого отрезка, где start <= point
            int idx = binarySearchFirst(segments, point);

            // проходим влево от найденного индекса
            int j = idx;
            while (j >= 0 && segments[j].start <= point) {
                if (segments[j].stop >= point) {
                    count++;
                }
                j--;
            }

            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
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
            // сортируем сначала по start, потом по stop
            if (this.start != other.start) {
                return this.start - other.start;
            }
            return this.stop - other.stop;
        }

    }
}
