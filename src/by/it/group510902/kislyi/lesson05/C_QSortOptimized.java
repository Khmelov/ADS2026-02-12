package by.it.group510902.kislyi.lesson05;

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
        Scanner scanner = new Scanner(stream);

        // Читаем входные данные
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // Читаем отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            // Нормализуем отрезки (начало <= конец)
            segments[i] = new Segment(Math.min(start, end), Math.max(start, end));
        }

        // Читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки с помощью оптимизированной быстрой сортировки
        quickSort3Way(segments, 0, n - 1);

        // Создаем массивы начал и концов
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Находим первый отрезок, который может покрывать точку
            // (первый отрезок с start <= point)
            int firstIndex = findFirstSegment(starts, point);

            if (firstIndex == -1) {
                result[i] = 0;
                continue;
            }

            // Считаем количество отрезков, которые покрывают точку
            // начиная с firstIndex
            int count = 0;
            for (int j = firstIndex; j < n && segments[j].start <= point; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        return result;
    }

    // Быстрая сортировка с 3-разбиением (оптимизированная для повторяющихся элементов)
    private void quickSort3Way(Segment[] arr, int left, int right) {
        // Элиминация хвостовой рекурсии - используем цикл вместо рекурсии для правой части
        while (left < right) {
            // Если массив маленький, используем сортировку вставками
            if (right - left < 10) {
                insertionSort(arr, left, right);
                break;
            }

            // Выбираем медиану из трех для лучшего опорного элемента
            int mid = left + (right - left) / 2;
            medianOfThree(arr, left, mid, right);

            // Опорный элемент - средний элемент после медианы
            Segment pivot = arr[mid];

            // 3-х разбиение: arr[left..lt-1] < pivot, arr[lt..gt] == pivot, arr[gt+1..right] > pivot
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

            // Рекурсивно сортируем левую часть (меньшие элементы)
            quickSort3Way(arr, left, lt - 1);

            // Хвостовая рекурсия - вместо рекурсии для правой части, обновляем границы и продолжаем цикл
            left = gt + 1;
        }
    }

    // Сортировка вставками для маленьких подмассивов
    private void insertionSort(Segment[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            Segment key = arr[i];
            int j = i - 1;
            while (j >= left && arr[j].compareTo(key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Выбор медианы из трех элементов
    private void medianOfThree(Segment[] arr, int a, int b, int c) {
        if (arr[a].compareTo(arr[b]) > 0) swap(arr, a, b);
        if (arr[a].compareTo(arr[c]) > 0) swap(arr, a, c);
        if (arr[b].compareTo(arr[c]) > 0) swap(arr, b, c);
        // Помещаем медиану в середину
        swap(arr, b, (a + c) / 2);
    }

    // Обмен элементов
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Бинарный поиск первого отрезка, который может покрывать точку
    // (первый отрезок с start <= point)
    private int findFirstSegment(int[] starts, int point) {
        int left = 0;
        int right = starts.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (starts[mid] <= point) {
                result = mid;
                left = mid + 1; // Ищем более правый подходящий отрезок
            } else {
                right = mid - 1;
            }
        }
        return result;
    }

    // Отрезок с реализацией сравнения по началу, а при равенстве - по концу
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment other) {
            // Сначала сравниваем по началу
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            }
            // Если начала равны, сравниваем по концу
            return Integer.compare(this.stop, other.stop);
        }

        @Override
        public String toString() {
            return "[" + start + ", " + stop + "]";
        }
    }
}