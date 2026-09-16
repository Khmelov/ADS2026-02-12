package by.it.group510901.kachur.lesson01.lesson05;

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
        // ---------- СОРТИРОВКА С 3-РАЗБИЕНИЕМ ----------
        sortSegments(segments, 0, n - 1);

        // Создаем массивы начал и концов (уже отсортированы)
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Сортируем концы отдельно (обычной сортировкой, но можно и 3-way)
        quickSort3Way(ends, 0, n - 1);

        // ---------- ПОИСК ДЛЯ КАЖДОЙ ТОЧКИ ----------
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Количество отрезков, начавшихся <= point
            int started = upperBound(starts, point);

            // Количество отрезков, закончившихся < point
            int ended = lowerBound(ends, point);

            result[i] = started - ended;
        }

        return result;
    }

    // ---------- БЫСТРАЯ СОРТИРОВКА С 3-РАЗБИЕНИЕМ И ЭЛИМИНАЦИЕЙ ХВОСТОВОЙ РЕКУРСИИ ----------

    private void sortSegments(Segment[] arr, int left, int right) {
        while (left < right) {
            int lt = left;
            int gt = right;
            Segment pivot = arr[left];
            int i = left + 1;

            // 3-разбиение (Dutch National Flag)
            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(arr, i++, lt++);
                } else if (cmp > 0) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }

            // Элиминация хвостовой рекурсии — сортируем меньшую часть рекурсивно,
            // а большую — в цикле
            if (lt - left < right - gt) {
                sortSegments(arr, left, lt - 1);
                left = gt + 1;
            } else {
                sortSegments(arr, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // ---------- БЫСТРАЯ СОРТИРОВКА ДЛЯ МАССИВА int (тоже с 3-разбиением) ----------

    private void quickSort3Way(int[] arr, int left, int right) {
        while (left < right) {
            int lt = left;
            int gt = right;
            int pivot = arr[left];
            int i = left + 1;

            while (i <= gt) {
                if (arr[i] < pivot) {
                    swap(arr, i++, lt++);
                } else if (arr[i] > pivot) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }

            if (lt - left < right - gt) {
                quickSort3Way(arr, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // ---------- БИНАРНЫЙ ПОИСК ----------

    private int upperBound(int[] arr, int x) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= x) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    private int lowerBound(int[] arr, int x) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < x) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!



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
