package by.it.group551002.loiko.lesson05;

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
        // сортируем отрезки с помощью улучшенной быстрой сортировки
        quickSort3Way(segments, 0, segments.length - 1);

// создаём массивы начал и концов для бинарного поиска
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

// для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // первый отрезок, у которого start > point
            int firstGreater = binarySearchFirstGreater(starts, point);

            if (firstGreater == 0) {
                result[i] = 0;
                continue;
            }

            int count = 0;
            for (int j = 0; j < firstGreater; j++) {
                if (stops[j] >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
        // бинарный поиск первого элемента > x
        private int binarySearchFirstGreater(int[] arr, int x) {
            int left = 0;
            int right = arr.length - 1;
            int result = arr.length;

            while (left <= right) {
                int mid = (left + right) / 2;
                if (arr[mid] > x) {
                    result = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            return result;
        }

// улучшенная быстрая сортировка с 3-разбиением и устранением хвостовой рекурсии
        private void quickSort3Way(Segment[] arr, int left, int right) {
            while (left < right) {
                int mid = (left + right) / 2;
                if (arr[left].compareTo(arr[mid]) > 0) swap(arr, left, mid);
                if (arr[left].compareTo(arr[right]) > 0) swap(arr, left, right);
                if (arr[mid].compareTo(arr[right]) > 0) swap(arr, mid, right);
                swap(arr, mid, right - 1);
                Segment pivot = arr[right - 1];

                int i = left;
                int j = right - 1;
                int k = left;

                while (k <= j) {
                    int cmp = arr[k].compareTo(pivot);
                    if (cmp < 0) {
                        swap(arr, i, k);
                        i++;
                        k++;
                    } else if (cmp > 0) {
                        swap(arr, j, k);
                        j--;
                    } else {
                        k++;
                    }
                }

                quickSort3Way(arr, left, i - 1);
                left = j + 1;
            }
        }

// вспомогательный метод для обмена элементов
        private void swap(Segment[] arr, int i, int j) {
            Segment temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }



    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
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
