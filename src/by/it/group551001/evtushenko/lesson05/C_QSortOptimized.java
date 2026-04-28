package by.it.group551001.evtushenko.lesson05;
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

        Pair[] combined = new Pair[segments.length * 2 + points.length];
        
        for(int i = 0; i < points.length; ++i) combined[i] = new Pair(points[i], 0, i);
        for(int i = 0; i < segments.length; ++i){
            combined[points.length + i] = new Pair(segments[i].start, -1, 0);
            combined[combined.length - i - 1] = new Pair(segments[i].stop, 1, 0);
        }

        qSort(combined, 0, combined.length - 1);

        for(int i = 0, j = 0, c = 0; i < points.length; ++j){
            if(combined[j].b == -1){
                ++c;
                continue;
            }

            if(combined[j].b == 1){
                --c;
                continue;
            }

            result[combined[j].i] = c;
            ++i;
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
            //подумайте, что должен возвращать компаратор отрезков
            return 0;
        }
    }

    <T extends Comparable<T>> void qSort(T[] a, int b, int e){
        while(b < e){
            int bt = b, et = e, i = b;
            T v = a[(b + e) / 2];
            while(i <= et){
                if(a[i].compareTo(v) < 0){
                    T t = a[i];
                    a[i] = a[bt];
                    a[bt] = t;
                    ++bt;
                    ++i;
                } else if(a[i].compareTo(v) > 0){
                    T t = a[i];
                    a[i] = a[et];
                    a[et] = t;
                    --et;
                } else ++i;
            }

            if(bt - b < e - et){
                qSort(a, b, bt - 1);
                b = et + 1;
            } else {
                qSort(a, et + 1, e);
                e = bt - 1;
            }
        }
    }

    private class Pair implements Comparable<Pair> {
        int a;
        int b;
        int i;

        Pair(int a, int b, int i) {
            this.a = a;
            this.b = b;
            this.i = i;
        }

        @Override
        public int compareTo(Pair o) {
            if(a == o.a) return Integer.compare(b, o.b);

            return Integer.compare(a, o.a);
        }
    }
}
