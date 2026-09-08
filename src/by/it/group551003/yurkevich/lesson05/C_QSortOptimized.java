package by.it.group551003.yurkevich.lesson05;

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
        quickSort3Way(segments, 0, n - 1);

        // Для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int x = points[i];

            // Бинарный поиск первого отрезка с start <= x
            int firstIndex = findFirstSegment(segments, x);

            if (firstIndex == -1) {
                result[i] = 0;
                continue;
            }

            // Считаем все отрезки, покрывающие точку
            int count = 0;
            for (int j = firstIndex; j < n && segments[j].start <= x; j++) {
                if (segments[j].stop >= x) {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    private int findFirstSegment(Segment[] segments, int x) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].start <= x) {
                result = mid;
                right = mid - 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            if (high - low + 1 < 10) {
                insertionSort(arr, low, high);
                break;
            }

            int lt = low;
            int gt = high;
            int i = low + 1;
            Segment pivot = arr[low];

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

            quickSort3Way(arr, low, lt - 1);

            low = gt + 1;
        }
    }

    private void insertionSort(Segment[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            Segment key = arr[i];
            int j = i - 1;

            while (j >= low && arr[j].compareTo(key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            }
            return Integer.compare(this.stop, other.stop);
        }
    }

}
