package by.it.group551002.kuzmenia.lesson05;

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

    private void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private void quickSort(Segment[] array, int left, int right)
    {
        while (left < right)
        {
            int lt = left, gt = right;
            Segment pivot = array[left];
            int i = left + 1;

            while (i <= gt)
            {
                int cmp = array[i].compareTo(pivot);
                if (cmp < 0)
                    swap(array, lt++, i++);
                else if (cmp > 0)
                    swap(array, i, gt--);
                else
                    i++;
            }

            if (lt - left < right - gt) {
                quickSort(array, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(array, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];

            // бинарный поиск первого возможного отрезка (где segment.start <= point)
            int left = 0, right = n - 1;
            int lastPossible = -1;
            while (left <= right)
            {
                int mid = (left + right) / 2;
                if (segments[mid].start <= point)
                {
                    lastPossible = mid;
                    left = mid + 1;
                }
                else
                {
                    right = mid - 1;
                }
            }

            int count = 0;
            for (int j = 0; j <= lastPossible; j++)
            {
                if (segments[j].stop >= point)
                {
                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment>
    {
        int start;
        int stop;

        Segment(int start, int stop)
        {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o)
        {
            if (this.start != o.start)
            {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

}
