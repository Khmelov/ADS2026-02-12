package by.it.group510902.zenkovich.lesson05;

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
        quickSort3Way(segments, 0, n - 1);


        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Бинарный поиск первого отрезка, у которого stop >= point
            int firstIndex = findFirstSegment(segments, point);

            if (firstIndex == -1) {
                result[i] = 0;
                continue;
            }

            // Бинарный поиск последнего отрезка, у которого start <= point
            int lastIndex = findLastSegment(segments, point);

            // Считаем количество отрезков от firstIndex до lastIndex
            int count = 0;
            for (int j = firstIndex; j <= lastIndex; j++) {
                if (segments[j].stop >= point && segments[j].start <= point) {
                    count++;
                }
            }

            result[i] = count;
        }





        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }



    private void quickSort3Way(Segment[] arr, int low, int high) {
        // Элиминация хвостовой рекурсии - используем цикл вместо рекурсии для правой части
        while (low < high) {
            // Разбиваем массив на 3 части: меньше опорного, равные опорному, больше опорного
            int[] pivots = partition3Way(arr, low, high);
            int lt = pivots[0];  // граница меньших
            int gt = pivots[1];  // граница больших

            // Рекурсивно сортируем левую часть (меньшие)
            quickSort3Way(arr, low, lt - 1);

            // Для правой части (большие) используем итерацию вместо рекурсии (устранение хвостовой рекурсии)
            low = gt + 1;
        }
    }

    // 3-разбиение массива относительно опорного элемента
    private int[] partition3Way(Segment[] arr, int low, int high) {
        // Используем медиану из трех для выбора опорного элемента (улучшает производительность)
        int mid = low + (high - low) / 2;
        Segment pivot = medianOfThree(arr, low, mid, high);

        int lt = low;      // граница элементов меньше опорного
        int i = low;       // текущий индекс
        int gt = high;     // граница элементов больше опорного

        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);

            if (cmp < 0) {
                // текущий элемент меньше опорного - меняем с lt
                swap(arr, lt, i);
                lt++;
                i++;
            } else if (cmp > 0) {
                // текущий элемент больше опорного - меняем с gt
                swap(arr, i, gt);
                gt--;
            } else {
                // текущий элемент равен опорному
                i++;
            }
        }

        return new int[]{lt, gt};
    }

    // Выбор медианы из трех элементов
    private Segment medianOfThree(Segment[] arr, int left, int mid, int right) {
        if (arr[left].compareTo(arr[mid]) > 0) {
            swap(arr, left, mid);
        }
        if (arr[left].compareTo(arr[right]) > 0) {
            swap(arr, left, right);
        }
        if (arr[mid].compareTo(arr[right]) > 0) {
            swap(arr, mid, right);
        }
        return arr[mid];
    }

    // Меняем местами два элемента
    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Бинарный поиск первого отрезка, который может содержать точку
    // (первый отрезок, у которого stop >= point)
    private int findFirstSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].stop >= point) {
                result = mid;
                right = mid - 1;  // ищем левее, чтобы найти первый
            } else {
                left = mid + 1;
            }
        }

        return result;
    }

    // Бинарный поиск последнего отрезка, который может содержать точку
    // (последний отрезок, у которого start <= point)
    private int findLastSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].start <= point) {
                result = mid;
                left = mid + 1;   // ищем правее, чтобы найти последний
            } else {
                right = mid - 1;
            }
        }

        return result;
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
