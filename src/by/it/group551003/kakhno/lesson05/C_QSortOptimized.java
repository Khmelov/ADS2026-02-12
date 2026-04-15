package by.it.group551003.kakhno.lesson05;

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
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        quickSort3Way(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int firstMatchIndex = findFirstMatchingSegment(segments, point);
            if (firstMatchIndex != -1) {
                int count = 0;
                for (int j = firstMatchIndex; j < n; j++) {
                    if (segments[j].start <= point && point <= segments[j].stop)
                        count++;
                    else if (segments[j].start > point)
                        break;
                }
                result[i] = count;
            } else {
                int count = 0;
                for (int j = 0; j < n; j++) {
                    if (segments[j].start > point)
                        break;
                    if (segments[j].start <= point && point <= segments[j].stop)
                        count++;
                }
                result[i] = count;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort3Way(Segment[] a, int lo, int hi) {
        if (hi <= lo) return;
        int lt = lo, gt = hi;
        Segment v = a[lo];
        int i = lo;
        while (i <= gt) {
            int cmp = a[i].compareTo(v);
            if (cmp < 0) exch(a, lt++, i++);
            else if (cmp > 0) exch(a, i, gt--);
            else i++;
        }

        if (lt - lo < hi - gt) {
            quickSort3Way(a, lo, lt - 1);
            quickSort3Way(a, gt + 1, hi);
        } else {
            quickSort3Way(a, gt + 1, hi);
            quickSort3Way(a, lo, lt - 1);
        }
    }

    private void exch(Segment[] a, int i, int j) {
        Segment swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private int findFirstMatchingSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int resultIndex = -1;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point && point <= segments[mid].stop) {
                resultIndex = mid;
                right = mid - 1;
            } else if (segments[mid].stop < point)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return resultIndex;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            if (this.start > this.stop) {
                int temp = this.start;
                this.start = this.stop;
                this.stop = temp;
            }
        }

        @Override
        public int compareTo(Object o) {
            //подумайте, что должен возвращать компаратор отрезков
            if (this.stop != ((Segment) o).stop)
                return Integer.compare(this.stop, ((Segment) o).stop);
            else
                return Integer.compare(this.start, ((Segment) o).start);
        }
    }

}
