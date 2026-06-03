package by.it.group551001.kuzminich.lesson5;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы:
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

    // Бинарный поиск первого отрезка, чей start > x
    private int findFirstGreater(int[] starts, int x) {
        int left = 0;
        int right = starts.length - 1;
        int result = starts.length;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (starts[mid] > x) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    // Бинарный поиск последнего отрезка, чей stop < x
    private int findLastLess(int[] stops, int x) {
        int left = 0;
        int right = stops.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (stops[mid] < x) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    // Обмен элементов
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Быстрая сортировка с 3-разбиением и элиминацией хвостовой рекурсии
    private void quickSort3Way(Segment[] arr, int left, int right) {
        while (left < right) {
            // Выбираем опорный элемент (медиана из трех для улучшения)
            int mid = left + (right - left) / 2;
            Segment pivot = arr[mid];

            // 3-разбиение: [left, lt] < pivot, [lt+1, gt-1] = pivot, [gt, right] > pivot
            int lt = left;
            int gt = right;
            int i = left;

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

            // Элиминация хвостовой рекурсии - рекурсивно сортируем меньшую часть
            // и продолжаем итеративно для большей
            if (lt - left < right - gt) {
                quickSort3Way(arr, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Читаем отрезки
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        // Читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки на месте с помощью 3-разбиения и элиминацией хвостовой рекурсии
        quickSort3Way(segments, 0, n - 1);

        // Создаем массивы начал и концов
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        // Для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Находим первый отрезок, чье начало > point
            int firstGreater = findFirstGreater(starts, point);

            // Находим последний отрезок, чей конец < point
            int lastLess = findLastLess(stops, point);

            // Количество отрезков, которые начались <= point и закончились >= point
            // = (количество отрезков с start <= point) - (количество отрезков с stop < point)
            result[i] = firstGreater - (lastLess + 1);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Отрезок с компаратором
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Нормализуем отрезок
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            // Сначала сравниваем по началу, потом по концу
            if (this.start != o.start) {
                return this.start - o.start;
            }
            return this.stop - o.stop;
        }
    }
}