package by.it.group510902.klimantovich.lesson01.lesson05;

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
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            // Гарантируем, что start <= end
            segments[i] = new Segment(Math.min(start, end), Math.max(start, end));
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        //Сортируем отрезки по началу с помощью оптимизированной быстрой сортировки
        // Используем 3-разбиение (Dutch National Flag algorithm) и элиминацию хвостовой рекурсии
        quickSort3Way(segments, 0, n - 1);
        //Создаем отдельный массив начал отрезков для бинарного поиска
        int[] starts = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
        }
        //Для каждой точки находим количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];
            // Находим первый отрезок, который может покрыть точку
            // (отрезок с start <= point)
            int firstIndex = findFirstSegment(starts, point);
            if (firstIndex == -1) {
                // Нет отрезков, которые начинаются не позже точки
                result[i] = 0;
                continue;
            }
            // Считаем количество отрезков от firstIndex до конца, которые покрывают точку
            // Отрезок покрывает точку, если start <= point <= stop
            int count = 0;
            for (int j = firstIndex; j < n; j++) {
                if (segments[j].start > point) {
                    break; // Дальнейшие отрезки начинаются позже точки
                }
                if (segments[j].stop >= point) {
                    count++; // Отрезок покрывает точку
                }
            }
            result[i] = count;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    // Быстрая сортировка с 3-разбиением (для эффективной обработки повторяющихся элементов)
    // и элиминацией хвостовой рекурсии для оптимизации использования стека
    private void quickSort3Way(Segment[] arr, int left, int right) {
        // Используем итеративный подход вместо рекурсивного для элиминации хвостовой рекурсии
        while (left < right) {
            // Выбираем опорный элемент (медиана из трех для лучшей производительности)
            int pivotIndex = medianOfThree(arr, left, right);
            Segment pivot = arr[pivotIndex];
            // Выполняем 3-разбиение: элементы < pivot, = pivot, > pivot
            // Возвращаем границы области с элементами равными pivot
            int[] bounds = partition3Way(arr, left, right, pivot);
            int lt = bounds[0]; // последний индекс области < pivot
            int gt = bounds[1]; // первый индекс области > pivot
            // Рекурсивно сортируем левую часть (элементы < pivot)
            // Используем элиминацию хвостовой рекурсии: сортируем меньшую часть рекурсивно,
            // а большую - итеративно
            if (lt - left < right - gt) {
                // Левая часть меньше - сортируем её рекурсивно
                quickSort3Way(arr, left, lt);
                // Переходим к правой части итеративно
                left = gt;
            } else {
                // Правая часть меньше - сортируем её рекурсивно
                quickSort3Way(arr, gt, right);
                // Переходим к левой части итеративно
                right = lt;
            }
        }
    }
    // Медиана из трех элементов (первый, средний, последний) для выбора опорного элемента
    private int medianOfThree(Segment[] arr, int left, int right) {
        int mid = left + (right - left) / 2;
        // Сравниваем и возвращаем индекс медианы
        if (arr[left].compareTo(arr[mid]) > 0) {
            swap(arr, left, mid);
        }
        if (arr[left].compareTo(arr[right]) > 0) {
            swap(arr, left, right);
        }
        if (arr[mid].compareTo(arr[right]) > 0) {
            swap(arr, mid, right);
        }

        // Помещаем медиану в конец (как опорный элемент)
        swap(arr, mid, right);
        return right;
    }
    // 3-разбиение массива (Dutch National Flag algorithm)
    // Возвращает массив из двух индексов: [lt, gt], где:
    // [left, lt] - элементы меньше опорного
    // [lt+1, gt-1] - элементы равные опорному
    // [gt, right] - элементы больше опорного
    private int[] partition3Way(Segment[] arr, int left, int right, Segment pivot) {
        int lt = left - 1;     // граница области меньше опорного
        int gt = right + 1;    // граница области больше опорного
        int i = left;          // текущий индекс
        while (i < gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                // Текущий элемент меньше опорного - меняем с lt+1
                lt++;
                swap(arr, lt, i);
                i++;
            } else if (cmp > 0) {
                // Текущий элемент больше опорного - меняем с gt-1
                gt--;
                swap(arr, i, gt);
                // i не увеличиваем, так как новый элемент на позиции i еще не проверен
            } else {
                // Текущий элемент равен опорному - просто двигаемся дальше
                i++;
            }
        }
        return new int[]{lt, gt};
    }
    // Вспомогательный метод для обмена элементов в массиве
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    // Бинарный поиск первого отрезка, который может покрыть точку
    // Ищем первый индекс, где start <= point
    // Возвращает -1, если таких отрезков нет
    private int findFirstSegment(int[] starts, int point) {
        int left = 0;
        int right = starts.length - 1;
        int result = -1;
        // Ищем самый левый отрезок с start <= point
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (starts[mid] <= point) {
                // Нашли подходящий отрезок, но может быть есть левее
                result = mid;
                right = mid - 1;
            } else {
                // Текущий отрезок начинается позже точки, ищем левее
                right = mid - 1;
            }
        }
        return result;
    }
    //отрезок
    private class Segment implements Comparable<Segment> {

        int start;
        int stop;
        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            // Сортируем сначала по началу отрезка
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            // Если начала равны, сортируем по концу
            return Integer.compare(this.stop, o.stop);
        }
    }
}