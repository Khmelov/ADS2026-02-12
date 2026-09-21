package by.it.group551001.belozorchik.lesson05;

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
        quick_sort(segments, 0, n - 1);

        // поиск для каждой точки с использованием бинарного поиска
        for (int i = 0; i < m; i++) {
            int value = points[i];
            int l = 0;
            int r = n - 1;
            int last_possible_idx = -1;

            // поиск для самой правой границы, где отрезок еще может включать точку
            while (l <= r) {
                int mid = l + (r - l) / 2;
                if (segments[mid].start <= value) {
                    last_possible_idx = mid; // запомнила индекс
                    l = mid + 1;           // дальше вправо
                } else {
                    r = mid - 1;
                }
            }

            // проверяю те отрезки, которые начались не позже точки
            if (last_possible_idx != -1) {
                for (int j = 0; j <= last_possible_idx; j++) {
                    if (segments[j].stop >= value) {
                        result[i]++;
                    }
                }
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public void quick_sort (Segment[] arr, int left, int right) {
        if (left >= right) return;
        int m = partition (arr, left, right);
        quick_sort(arr, left, m - 1);
        quick_sort(arr, m + 1, right);
    }
    private int partition (Segment[] arr, int left, int right) {
        Segment x = arr[left];
        int j = left;

        for (int i = (left + 1); i <= right; i++) {
            if (arr[i].compareTo(x) <= 0) {
                j = j + 1;
                Segment temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
            }
        }
        Segment temp = arr[left];
        arr[left] = arr[j];
        arr[j] = temp;
        return j;
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
            // сортируем по началу, если начала равны — по концу
            Segment other = (Segment) o;
            if (this.start != other.start) return Integer.compare(this.start, other.start);
            return Integer.compare(this.stop, other.stop);
        }
    }

}
