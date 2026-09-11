package by.it.group551001.akynchits.lesson05;

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
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        optimizedQuickSort(starts, 0, n - 1);
        optimizedQuickSort(stops, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int started = countLessOrEqual(starts, p);
            int ended = countLess(stops, p);
            result[i] = started - ended;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    private void optimizedQuickSort(int[] a, int left, int right) {
        while (left < right) {
            // 3-way partitioning (lt - начало равных, gt - конец равных)
            int lt = left, i = left + 1, gt = right;
            int pivot = a[left];

            while (i <= gt) {
                if (a[i] < pivot) swap(a, lt++, i++);
                else if (a[i] > pivot) swap(a, i, gt--);
                else i++;
            }

            // Элиминация хвостовой рекурсии:
            // Рекурсивно вызываем для меньшей части, итерируем в большей
            if (lt - left < right - gt) {
                optimizedQuickSort(a, left, lt - 1);
                left = gt + 1;
            } else {
                optimizedQuickSort(a, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    private int countLessOrEqual(int[] arr, int x) {
        int l = 0, r = arr.length - 1;
        int res = 0;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (arr[m] <= x) {
                res = m + 1;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return res;
    }

    // Бинарный поиск: количество элементов < x
    private int countLess(int[] arr, int x) {
        int l = 0, r = arr.length - 1;
        int res = 0;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (arr[m] < x) {
                res = m + 1;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return res;
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
            //подумайте, что должен возвращать компаратор отрезков
            Segment other = (Segment) o;

            // Сначала сравниваем по началу отрезка
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            }
            // Если начала равны, сравниваем по концу отрезка
            return Integer.compare(this.stop, other.stop);
        }
    }

}
