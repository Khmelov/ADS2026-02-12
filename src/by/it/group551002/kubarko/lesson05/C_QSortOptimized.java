package by.it.group551002.kubarko.lesson05;

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
        //число отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // 1. Сортировка массива отрезков на месте
        quickSort(segments, 0, n - 1);

        // 2. Поиск для каждой точки
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Бинарный поиск
            int lastPossibleIdx = findLastStartBeforeOrAt(segments, point);

            int count = 0;
            if (lastPossibleIdx != -1) {
                // Проверяем все отрезки, которые начинаются до точки или в ней
                // Среди них ищем те, которые заканчиваются после точки или в ней
                for (int j = 0; j <= lastPossibleIdx; j++) {
                    if (segments[j].stop >= point) {
                        count++;
                    }
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Оптимизированный QuickSort
    private void quickSort(Segment[] a, int left, int right) {
        while (left < right) {
            // Выбор опорного элемента
            Segment pivot = a[left + (right - left) / 2];

            int lt = left;
            int gt = right;
            int i = left;
            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(a, lt++, i++);
                } else if (cmp > 0) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }

            // Элиминация хвостовой рекурсии
            if (lt - left < right - gt) {
                quickSort(a, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(a, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // Бинарный поиск правого края для условия start <= point
    private int findLastStartBeforeOrAt(Segment[] a, int point) {
        int low = 0;
        int high = a.length - 1;
        int res = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (a[mid].start <= point) {
                res = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return res;
    }

    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Нормализация отрезка, если начало больше конца
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            // Сортируем в первую очередь по началу отрезка
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            // Во вторую - по концу
            return Integer.compare(this.stop, o.stop);
        }
    }
}