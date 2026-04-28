package by.it.group551003.popko.lesson05;

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
            int stop = scanner.nextInt();
            segments[i] = new Segment(start, stop);
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

            int left = 0;
            int right = n - 1;
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

            if (firstIndex >= 0) {
                int count = 0;
                for (int j = 0; j <= firstIndex; j++) {
                    if (segments[j].stop >= point) {
                        count++;
                    }
                }
                result[i] = count;
            } else {
                result[i] = 0;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort3Way(Segment[] arr, int low, int high) {
        while (low < high) {
            int pivotIndex = medianOfThree(arr, low, high);
            Segment pivot = arr[pivotIndex];

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

            int leftSize = lt - low;
            int rightSize = high - gt;

            if (leftSize < rightSize) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private int medianOfThree(Segment[] arr, int low, int high) {
        int mid = low + (high - low) / 2;
        if (arr[mid].compareTo(arr[low]) < 0) {
            swap(arr, low, mid);
        }
        if (arr[high].compareTo(arr[low]) < 0) {
            swap(arr, low, high);
        }
        if (arr[high].compareTo(arr[mid]) < 0) {
            swap(arr, mid, high);
        }
        swap(arr, mid, low);
        return low;
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
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.start, other.start);
        }
        @Override
        public String toString() {
            return "[" + start + ", " + stop + "]";
        }
    }

}
