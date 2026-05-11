package by.it.group551003.tarasau.lesson05;

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
        theQuickerSort(segments, n);

        result = countSegmentsForPoints(segments, n, points, m);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // The Quicker Sort™ (technically, not really, just no stackoverflow error, should be faster though?)
    public void theQuickerSort(Segment[] segments, int n) {
        int[] stack = new int[n * 2];  // soo twice the size to store 2 pointers for both halves' recursions
        int sp = -1;                   // stack pointer
        stack[++sp] = 0;
        stack[++sp] = n-1;             //same as qsort(arr, 0, n-1);

        while (sp > -1) {
            int end = stack[sp--];
            int start = stack[sp--];   // starting the "recursion"

            if (start < end) {         // if pointers cross we leave
                int p = partition(segments, start, end);

                if (p - start < end - p) {  // replace recursion kinda, qsort(arr , p+1, end), qsort(arr, start, p-1)
                    stack[++sp] = p + 1;    // if right half has more elems than left one, use right first
                    stack[++sp] = end;
                    stack[++sp] = start;
                    stack[++sp] = p - 1;
                } else {                   // if left half has more elems than left one, use left first
                    stack[++sp] = start;
                    stack[++sp] = p - 1;
                    stack[++sp] = p + 1;
                    stack[++sp] = end;
                }   // so this method gives us O(log(n)) stack size in general and O(n) at worst i guess
            }
        }
    }

    public int partition(Segment[] segments, int start, int end) {
        boolean unsorted = true;  // pointers haven't crossed... yet
        int i = start;
        int j = end+1;        // end+1 to account for the --j
        Segment p = segments[i];

        while (unsorted) {    // doing till pointers cross
            // move left pointer till we find elem or it reaches the end
            while (i < end && segments[++i].compareTo(p) < 0);

            // move right pointer till we find elem or it reaches the beginning
            while (j > start && segments[--j].compareTo(p) > 0);

            if (i >= j) unsorted = false;

            if (unsorted) swap(segments, i, j);
        }

        swap(segments, start, j);  // we've reserved pivot pos for this, move it at j aka the middle of sorted thing

        return j;
    }

    public void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
    }

    public int[] countSegmentsForPoints(Segment[] segments, int n, int[] points, int m) {
        int[] result = new int[m];

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int lp = 0;
            int rp = n;

            while (lp < rp) {
                int mid = lp + (rp - lp) / 2;

                if (segments[mid].start-1 < point) {
                    lp = mid + 1;  // the segments in left half (including mid) aren't needed, search in (mid+1, rp)
                } else {
                    rp = mid;      // the segments in right half aren't needed, search in (lp, mid);
                }
            }

            int count = 0;
            for (int j = 0; j < lp; j++) {
                if (segments[j].stop >= point) {  // each beginning should now include point, check the ends
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    //отрезок
    public class Segment implements Comparable<Segment> {  // yeah, i've made it not so OOP, haven't i? (removed private)
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start > stop) {
                this.stop = start;
                this.start = stop;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {  // why'd ya even change it to accept (Object o)? i may be clueless
            if (this.start != o.start) {  // alr ig we prioritise the beginning overall, and only then the end
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }

}
