package by.it.group510902.shust.lesson05;

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
        quickSort3(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Бинарный поиск: ищем самый правый отрезок, который начинается до или в точке
            int left = 0;
            int right = n - 1;
            int rightmostStart = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    rightmostStart = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            if (rightmostStart != -1) {
                // Идем влево и проверяем оставшуюся часть условия
                for (int j = rightmostStart; j >= 0; j--) {
                    if (segments[j].stop >= point) {
                        count++;
                    }
                }
            }
            result[i] = count;
        }

        return result;
    }

    private void quickSort3(Segment[] arr, int l, int r) {
        while (l < r) {
            int[] m = partition3(arr, l, r);
            if (m[0] - l < r - m[1]) {
                quickSort3(arr, l, m[0] - 1);
                l = m[1] + 1;
            } else {
                quickSort3(arr, m[1] + 1, r);
                r = m[0] - 1;
            }
        }
    }

    private int[] partition3(Segment[] arr, int l, int r) {
        Segment pivot = arr[l];
        int lt = l;
        int gt = r;
        int i = l;
        while (i <= gt) {
            int cmp = arr[i].compareTo(pivot);
            if (cmp < 0) {
                swap(arr, lt++, i++);
            } else if (cmp > 0) {
                swap(arr, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
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
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            }
            return Integer.compare(this.stop, other.stop);
        }
    }

}
