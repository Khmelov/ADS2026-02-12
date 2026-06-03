package by.it.group551001.mousik_aleksey.lesson5.lesson05;

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
        // --- 1. Компаратор для Segment ---
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (segments[i].start > segments[j].start ||
                        (segments[i].start == segments[j].start && segments[i].stop > segments[j].stop)) {

                    Segment tmp = segments[i];
                    segments[i] = segments[j];
                    segments[j] = tmp;
                }
            }
        }

        int left = 0, right = n - 1;
        while (left < right) {
            int lt = left, gt = right;
            Segment pivot = segments[left];
            int i = left;

            while (i <= gt) {
                if (segments[i].start < pivot.start ||
                        (segments[i].start == pivot.start && segments[i].stop < pivot.stop)) {

                    Segment tmp = segments[lt];
                    segments[lt] = segments[i];
                    segments[i] = tmp;
                    lt++; i++;

                } else if (segments[i].start > pivot.start ||
                        (segments[i].start == pivot.start && segments[i].stop > pivot.stop)) {

                    Segment tmp = segments[i];
                    segments[i] = segments[gt];
                    segments[gt] = tmp;
                    gt--;

                } else {
                    i++;
                }
            }

            if (lt - left < right - gt) {
                right = lt - 1;
                left = gt + 1;
            } else {
                left = gt + 1;
                right = lt - 1;
            }
        }

        for (int i = 0; i < m; i++) {
            int p = points[i];

            int l = 0, r = n - 1;
            int pos = -1;

            while (l <= r) {
                int mid = (l + r) / 2;
                if (segments[mid].start <= p) {
                    pos = mid;
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }

            if (pos == -1) {
                result[i] = 0;
                continue;
            }

            int count = 0;
            for (int j = pos; j < n && segments[j].start <= p; j++) {
                if (segments[j].stop >= p) count++;
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
            Segment s = (Segment) o;
            if (this.start != s.start) return this.start - s.start;
            return this.stop - s.stop;
        }
    }

}
