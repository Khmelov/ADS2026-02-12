package by.it.group551004.velichko.lesson05;

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

        // Сортируем отрезки по началу с использованием 3-разбиения и элиминацией хвостовой рекурсии
        quickSort3Way(segments, 0, n - 1);

        // Создаем массивы начал и концов для бинарного поиска
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Находим первый отрезок, который может покрывать точку (start <= point)
            int left = findFirstSegment(starts, point);
            if (left == -1) {
                result[i] = 0;
                continue;
            }
            // Считаем количество отрезков с start <= point и stop >= point
            int count = 0;
            for (int j = left; j < n && starts[j] <= point; j++) {
                if (ends[j] >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] pivotRange = partition3Way(arr, low, high);
            // Рекурсивно сортируем левую часть
            quickSort3Way(arr, low, pivotRange[0] - 1);
            // Хвостовая рекурсия - сортируем правую часть в следующей итерации цикла
            low = pivotRange[1] + 1;
        }
    }

    // 3-разбиение: элементы меньше пивота, равные пивоту, больше пивота
    private int[] partition3Way(Segment[] arr, int low, int high) {
        Segment pivot = arr[low];
        int lt = low;      // arr[low..lt-1] < pivot
        int i = low + 1;   // arr[lt..i-1] == pivot
        int gt = high;     // arr[gt+1..high] > pivot

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, lt, i);
                lt++;
                i++;
            } else if (cmp > 0) {
                swap(arr, i, gt);
                gt--;
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Бинарный поиск первого отрезка с start <= point
    private int findFirstSegment(int[] starts, int point) {
        int left = 0, right = starts.length - 1;
        int result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (starts[mid] <= point) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            return Integer.compare(this.start, other.start);
        }
    }

}
