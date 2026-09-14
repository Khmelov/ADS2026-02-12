package by.it.group510902.vinnichuk.lesson05;

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
        // сортирую отрезки
        quickSort(segments, 0, n - 1);

        // считаю покрытие для каждой точки
        for (int i = 0; i < m; i++) {
            result[i] = countIntersections(segments, points[i]);
        }



        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // 3-разбиение и элиминация хвостовой рекурсии
    private void quickSort(Segment[] a, int left, int right) {
        while (left < right) {
            int[] m = partition(a, left, right);
            // оптимизирую стек вызовов
            if (m[0] - left < right - m[1]) {
                quickSort(a, left, m[0] - 1);
                left = m[1] + 1;
            } else {
                quickSort(a, m[1] + 1, right);
                right = m[0] - 1;
            }
        }
    }

    // разбиение на три части
    private int[] partition(Segment[] a, int left, int right) {
        Segment pivot = a[left];
        int lt = left;
        int i = left;
        int gt = right;
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
        return new int[]{lt, gt};
    }

    private void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // бинарный поиск и подсчет отрезков
    private int countIntersections(Segment[] segments, int point) {
        int count = 0;
        int l = 0;
        int r = segments.length - 1;
        int firstIndex = -1;

        // ищу первый отрезок, где старт <= точки
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (segments[mid].start <= point) {
                firstIndex = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }

        // если нашелся, считаю все подходящие слева направо
        if (firstIndex != -1) {
            for (int i = firstIndex; i >= 0; i--) {
                if (segments[i].stop >= point) {
                    count++;
                }
            }
        }
        return count;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            //правильный порядок координат
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            // сортирую по началу отрезков
            return Integer.compare(this.start, other.start);
        }
    }

}
