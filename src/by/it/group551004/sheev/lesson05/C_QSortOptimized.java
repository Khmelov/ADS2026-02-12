package by.it.group551004.sheev.lesson05;

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

        // Для каждой точки находим количество отрезков, содержащих ее
        for (int i = 0; i < m; i++) {
            int point = points[i];
            result[i] = countSegmentsContainingPoint(segments, point);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Оптимизированная быстрая сортировка с элиминацией хвостовой рекурсии
    private void quickSort(Segment[] arr, int low, int high) {
        while (low < high) {
            // Используем 3-разбиение
            int[] partitionIndices = threeWayPartition(arr, low, high);
            int lt = partitionIndices[0]; // конец левой части
            int gt = partitionIndices[1]; // начало правой части

            // Рекурсивно сортируем меньшую часть, а большую обрабатываем итеративно
            if (lt - low < high - gt) {
                quickSort(arr, low, lt - 1);
                low = gt + 1; // хвостовая рекурсия для правой части
            } else {
                quickSort(arr, gt + 1, high);
                high = lt - 1; // хвостовая рекурсия для левой части
            }
        }
    }

    // 3-разбиение (Dutch National Flag algorithm)
    private int[] threeWayPartition(Segment[] arr, int low, int high) {
        Segment pivot = arr[low + (high - low) / 2]; // выбираем средний элемент как опорный
        int lt = low;     // граница элементов < pivot
        int i = low;      // текущий элемент
        int gt = high;    // граница элементов > pivot

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
        return new int[]{lt, gt};
    }

    // Бинарный поиск первого отрезка, который может содержать точку
    private int findFirstSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = segments.length;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].stop >= point) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    // Подсчет количества отрезков, содержащих точку
    private int countSegmentsContainingPoint(Segment[] segments, int point) {
        // Находим первый потенциально подходящий отрезок
        int startIdx = findFirstSegment(segments, point);
        int count = 0;

        // Считаем все подходящие отрезки от найденного индекса
        for (int i = startIdx; i < segments.length; i++) {
            if (segments[i].start <= point) {
                count++;
            } else {
                break; // отрезки отсортированы, дальше start будет только больше
            }
        }
        return count;
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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

}
