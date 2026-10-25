package by.it.group551003.sysoev.lesson05;

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
        int[] starts = new int[n];
        int[] stops = new int[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        quickSort(starts, 0, n - 1);
        quickSort(stops, 0, n - 1);

        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
            int countStart = count(starts, points[i] + 1);
            int countStop = count(stops, points[i]);
            result[i] = countStart - countStop;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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
    private int count(int[] arr, int value) {
        int l = 0, r = arr.length - 1;
        int cnt = 0;
        while (l <= r) {
            int m = (l + r) / 2;
            if (arr[m] < value) {
                cnt = m + 1;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return cnt;
    }

    private void quickSort(int[] arr, int low, int high) {
        while (low < high) {
            int pivot = arr[low];
            int lt = low, gt = high, i = low + 1;

            while (i <= gt) {
                if (arr[i] < pivot) {
                    int temp = arr[lt]; arr[lt] = arr[i]; arr[i] = temp;
                    lt++; i++;
                } else if (arr[i] > pivot) {
                    int temp = arr[i]; arr[i] = arr[gt]; arr[gt] = temp;
                    gt--;
                } else {
                    i++;
                }
            }

            if (lt - low < high - gt) {
                quickSort(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

}
