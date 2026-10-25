package by.it.group510901.pshanichny.lesson05;

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
        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++)
            result[i] = countSegments(segments, points[i]);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    void quickSort(Segment[] arr, int left, int right) {
        while (left < right) {
            int pivot = arr[(left + right) / 2].start;
            int lt = left;   // граница левой части (меньше pivot)
            int gt = right;  // граница правой части (больше pivot)
            int i = left;    // текущий элемент

            // 3-разбиение
            while (i <= gt) {
                if (arr[i].start < pivot) {
                    swap(arr, lt++, i++);
                } else if (arr[i].start > pivot) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }
            // после цикла: arr[left..lt-1] < pivot
            //              arr[lt..gt]     == pivot
            //              arr[gt+1..right] > pivot

            // рекурсивно сортируем меньшую часть
            quickSort(arr, left, lt - 1);
            // элиминация хвостовой рекурсии — большую часть через цикл
            left = gt + 1;
        }
    }

    // меняем два элемента местами
    void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // считаем сколько отрезков покрывают точку
    int countSegments(Segment[] segments, int point) {
        // бинарный поиск первого отрезка у которого start <= point
        int left = 0;
        int right = segments.length - 1;
        int first = -1;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (segments[mid].start <= point) {
                first = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        if (first == -1) return 0;  // все отрезки начинаются после точки

        // линейно считаем сколько из найденных отрезков ещё не закончились
        int count = 0;
        for (int i = 0; i <= first; i++)
            if (segments[i].stop >= point)
                count++;

        return count;
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
            return this.start - other.start;
        }
    }

}
