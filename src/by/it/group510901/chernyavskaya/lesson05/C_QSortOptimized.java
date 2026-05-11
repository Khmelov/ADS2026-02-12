package by.it.group510901.chernyavskaya.lesson05;

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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
        // число отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        // число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // читаем сами отрезки
        for (int i = 0; i < n; i++) {
            // читаем начало и конец каждого отрезка
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            // если концы в обратном порядке, исправляем
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }
            segments[i] = new Segment(start, end);
        }

        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки с помощью оптимизированной быстрой сортировки
        quickSort3Way(segments, 0, n - 1);

        // Для каждой точки находим количество покрывающих её отрезков с помощью бинарного поиска
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Находим первый отрезок, который может содержать точку
            // (первый отрезок с start <= point)
            int firstIndex = findFirstSegment(segments, point);

            // Если такой отрезок не найден, результат = 0
            if (firstIndex == -1) {
                result[i] = 0;
                continue;
            }

            // Считаем количество отрезков, содержащих точку,
            // начиная с firstIndex и пока start <= point
            int count = 0;
            for (int j = firstIndex; j < n && segments[j].start <= point; j++) {
                if (point <= segments[j].stop) {
                    count++;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка с 3-разбиением
    private void quickSort3Way(Segment[] arr, int left, int right) {
        while (left < right) {
            // Выбираем опорный элемент (медиана из трех для улучшения производительности)
            Segment pivot = medianOfThree(arr, left, right);

            // Выполняем 3-разбиение относительно pivot
            // lt - индекс последнего элемента < pivot
            // gt - индекс первого элемента > pivot
            int lt = left;      // левая граница
            int gt = right;     // правая граница
            int i = left;       // текущий индекс

            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) {
                    // элемент меньше pivot - меняем с lt и двигаем lt и i
                    swap(arr, lt, i);
                    lt++;
                    i++;
                } else if (cmp > 0) {
                    // элемент больше pivot - меняем с gt и двигаем gt
                    swap(arr, i, gt);
                    gt--;
                } else {
                    // элемент равен pivot - просто двигаем i
                    i++;
                }
            }
            int leftSize = lt - left;          // размер левой части
            int rightSize = right - gt;        // размер правой части

            if (leftSize < rightSize) {
                // Левая часть меньше - обрабатываем её рекурсивно
                quickSort3Way(arr, left, lt - 1);
                // Устанавливаем left для следующей итерации цикла (обработка правой части)
                left = gt + 1;
            } else {
                // Правая часть меньше - обрабатываем её рекурсивно
                quickSort3Way(arr, gt + 1, right);
                // Устанавливаем right для следующей итерации цикла (обработка левой части)
                right = lt - 1;
            }
        }
    }

    // Выбор медианы из трех элементов (left, mid, right)
    private Segment medianOfThree(Segment[] arr, int left, int right) {
        int mid = left + (right - left) / 2;

        // Сортируем три элемента
        if (arr[mid].compareTo(arr[left]) < 0) {
            swap(arr, left, mid);
        }
        if (arr[right].compareTo(arr[left]) < 0) {
            swap(arr, left, right);
        }
        if (arr[right].compareTo(arr[mid]) < 0) {
            swap(arr, mid, right);
        }

        // Возвращаем медиану (средний элемент)
        return arr[mid];
    }

    // Вспомогательный метод для обмена элементов
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Бинарный поиск первого отрезка, который может содержать точку
    // Ищем первый отрезок с start <= point
    // Возвращает индекс первого подходящего отрезка или -1, если такого нет
    private int findFirstSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].start <= point) {
                // Этот отрезок подходит, но может быть есть более ранний
                result = mid;
                // Ищем в левой половине (более ранние отрезки)
                right = mid - 1;
            } else {
                // start > point, нужно искать слева
                right = mid - 1;
            }
        }

        return result;
    }

    // отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // Сначала сравниваем по start
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            // Если start равны, сравниваем по stop
            return Integer.compare(this.stop, o.stop);
        }
    }

}