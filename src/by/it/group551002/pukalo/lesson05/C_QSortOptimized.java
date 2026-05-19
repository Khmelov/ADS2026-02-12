package by.it.group551002.pukalo.lesson05;

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

        // ПОИСК: для каждой точки находим количество покрывающих её отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Шаг 1: бинарный поиск последнего отрезка с start <= point
            int lastIndex = findLastWithStartLE(segments, point);

            if (lastIndex == -1) {
                result[i] = 0;
                continue;
            }

            // Шаг 2: бинарный поиск первого отрезка с stop >= point среди [0..lastIndex]
            int firstValidIndex = findFirstWithStopGE(segments, 0, lastIndex, point);

            if (firstValidIndex == -1) {
                result[i] = 0;
                continue;
            }

            // Шаг 3: считаем все отрезки, содержащие точку
            // (двигаемся от первого найденного влево и вправо)
            result[i] = countContainingSegments(segments, firstValidIndex, lastIndex, point);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private void quickSort(Segment[] arr, int left, int right) {
        while (left < right) {
            Segment pivot = arr[(left + right) / 2];

            int lt = left, gt = right, i = left;
            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0)
                    swap(arr, lt++, i++);
                else if (cmp > 0)
                    swap(arr, i, gt--);
                else
                    i++;
            }

            if (lt - left < right - gt) {
                quickSort(arr, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(arr, gt + 1, right);
                right = lt - 1;
            }
        }
    }
    private int findLastWithStartLE(Segment[] arr, int point) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid].start <= point) {
                result = mid;
                left = mid + 1;  // ищем дальше вправо
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    /**
     * Бинарный поиск первого отрезка с stop >= point в диапазоне [left, right]
     * @return индекс первого подходящего отрезка или -1
     */
    private int findFirstWithStopGE(Segment[] arr, int left, int right, int point) {
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid].stop >= point) {
                result = mid;
                right = mid - 1;  // ищем левее, т.к. нужен первый
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    /**
     * Подсчет количества отрезков, содержащих точку,
     * начиная с firstValidIndex и расширяясь влево и вправо
     * среди отрезков с индексами [0..lastIndex]
     */
    private int countContainingSegments(Segment[] segments, int firstValid, int lastIndex, int point) {
        int count = 0;

        // Считаем от firstValid вправо до lastIndex
        for (int i = firstValid; i <= lastIndex; i++) {
            if (segments[i].start <= point && segments[i].stop >= point) {
                count++;
            }
        }

        // Считаем от firstValid-1 влево до 0
        for (int i = firstValid - 1; i >= 0; i--) {
            if (segments[i].start <= point && segments[i].stop >= point) {
                count++;
            }
        }

        return count;
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
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
            return Integer.compare(this.start, other.start);
            //подумайте, что должен возвращать компаратор отрезков
        }
    }

}
