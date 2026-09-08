package by.it.group510902.strekha.lesson05;

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

    private void swap(Segment[] arr, int i, int j) {
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            Segment pivot = arr[low];
            int i = low;

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

            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private int upperBoundStart(Segment[] segments, int point) {
        int left = 0, right = segments.length;
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (segments[mid].start <= point) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
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
            int p = points[i];
            int limit = upperBoundStart(segments, p);
            int count = 0;
            for (int j = 0; j < limit; j++) {
                if (segments[j].stop >= p) {
                    count++;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;
        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

}