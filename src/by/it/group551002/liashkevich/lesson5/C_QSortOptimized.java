package by.it.group551002.liashkevich.lesson5;

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
        QuickSort(segments, 0, n-1);
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;
            int idx = BinarySearch(segments, point);
            if (idx != -1) {
                for (int j = idx; j < n && segments[j].start <= point; j++)
                    if (segments[j].stop >= point)
                        count++;
            }
            result[i] = count;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    void Swap(Segment[] A, int i, int j) {
        // Swapping elements
        Segment Temp = A[i];
        A[i] = A[j];
        A[j] = Temp;
    }

    int BinarySearch(Segment[] segs, int point) {
        int Left = 0;
        int Right = segs.length - 1;
        int result = -1;
        while (Left <= Right) {
            int Median = Left + (Right - Left) / 2;
            if (segs[Median].start <= point) {
                result = Median;
                Right = Median - 1;
            }
            else
                Right = Median - 1;
        }
        return result;
    }

    void QuickSort(Segment[] A, int Low, int High) {
        while (Low < High) {
            Segment Pivot = A[Low];
            int lt = Low;
            int gt = High;
            int i = Low + 1;
            while (i <= gt) {
                int cmp = A[i].compareTo(Pivot);
                if (cmp < 0) {
                    Swap(A, lt, i);
                    lt++;
                    i++;
                } else if (cmp > 0) {
                    Swap(A, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }
            QuickSort(A, Low, lt - 1);
            Low = gt + 1;
        }
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
            //подумайте, что должен возвращать компаратор отрезков
            if (this.start != other.start)
                return Integer.compare(this.start, other.start);
            return Integer.compare(this.stop, other.stop);
        }
    }

}
