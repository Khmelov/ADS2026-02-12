package by.it.group551001.biarezina.lesson05;

import org.hamcrest.core.IsEqual;

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

    private static int partition(Segment[] a, int l, int r) {
        Segment x = a[l];
        int j = l;

        for (int i = l + 1; i <= r; i++) {
            if (x.compareTo(a[i]) != -1) {
                j++;
                Segment temp = a[j];
                a[j] = a[i];
                a[i] = temp;
            }
        }

        Segment temp = a[l];
        a[l] = a[j];
        a[j] = temp;

        return j;
    }


    public static void quickSort(Segment[] a, int l, int r) {
        if (l >= r) {
            return;
        }

        int m = partition(a, l, r);

        quickSort(a, l, m - 1);

        quickSort(a, m + 1, r);
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

        quickSort(segments, 0, n-1);

        for (int i = 0; i < m; i++) {
            int val = points[i];
            int left = 0;
            int right = n-1;
            int lastPossibleIdx = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= val) {
                    lastPossibleIdx = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            // Проверяем все найденные отрезки (их может быть много)
            for (int j = lastPossibleIdx; j >= 0; j--) {
                if (segments[j].stop >= val) {
                    result[i]++;
                }
            }
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<C_QSortOptimized.Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(C_QSortOptimized.Segment o) {
            if (this.start < o.start){
                return -1;
            } else if (this.start > o.start){
                return 1;
            } else if (this.stop < o.stop){
                return -1;
            } else if (this.stop > o.stop) {
                return 1;
            }
            //подумайте, что должен возвращать компаратор отрезков

            return 0;
        }
    }

}
