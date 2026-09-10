package by.it.group510902.karcheuski.lesson05;

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

        // Быстрая сортировка с 3-разбиением на месте
        quickSort3Way(segments, 0, n - 1);

        // Создаем массив концов для бинарного поиска
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            ends[i] = segments[i].stop;
        }

        // Для каждой точки находим количество отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Бинарный поиск первого отрезка с start <= point
            int left = 0, right = n - 1;
            int firstIndex = -1;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (segments[mid].start <= point) {
                    firstIndex = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            if (firstIndex == -1) {
                result[i] = 0;
                continue;
            }

            // Среди отрезков [0..firstIndex] ищем те, у которых stop >= point
            int count = 0;
            for (int j = 0; j <= firstIndex; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    // Быстрая сортировка с 3-разбиением (Dijkstra 3-way partitioning)
    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            // Выбираем опорный элемент
            int mid = low + (high - low) / 2;
            Segment pivot = medianOfThree(arr, low, mid, high);

            // 3-разбиение: [low..lt-1] < pivot, [lt..gt] == pivot, [gt+1..high] > pivot
            int lt = low;
            int gt = high;
            int i = low;

            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(arr, lt, i);
                    lt++;
                    i++;
                } else if (cmp > 0) {
                    swap(arr, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }

            // Рекурсивно сортируем левую и правую части
            // Устраняем хвостовую рекурсию - сортируем меньшую часть рекурсивно,
            // а большую - итеративно
            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private Segment medianOfThree(Segment[] arr, int low, int mid, int high) {
        if (arr[low].compareTo(arr[mid]) > 0) swap(arr, low, mid);
        if (arr[low].compareTo(arr[high]) > 0) swap(arr, low, high);
        if (arr[mid].compareTo(arr[high]) > 0) swap(arr, mid, high);
        return arr[mid];
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
            //подумайте, что должен возвращать компаратор отрезков
            return 0;
        }
    }

}
