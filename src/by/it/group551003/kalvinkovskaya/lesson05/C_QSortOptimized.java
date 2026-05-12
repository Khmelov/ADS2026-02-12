package by.it.group551003.kalvinkovskaya.lesson05;

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

        for (int i = 0; i < m; i++) {
            result[i] = countContaining(segments, points[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            int lt = low;
            int gt = high;
            Segment pivot = arr[low];
            int i = low + 1;

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
            //если левая часть меньше - рекурсией сортируем правую, левую - итерационно
            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;
            //если правая часть меньше либо части равны - рекурсией сортируем левую, правую - итерационно
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private int binarySearchRightmost(Segment[] segments, int point) {
        int low = 0, high = segments.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    private int countContaining(Segment[] segments, int point) {
        int rightmost = binarySearchRightmost(segments, point);

        if (rightmost < 0) {
            return 0;
        }

        int count = 0;
        for (int i = 0; i <= rightmost; i++) {
            if (segments[i].stop >= point) {
                count++;
            }
        }
        return count;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) return Integer.compare(this.start, o.start);
            return Integer.compare(this.stop, o.stop);
        }
    }

}
