package by.it.group551003.parkhaniuk.lesson05;

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
        quickSort(segments, 0, n - 1);

        for(int i = 0; i < points.length; i++)
        {
            int point = points[i];
            int left = 0, right = n;
            while (left < right)
            {
                int mid = left + (right - left) / 2;
                if (point >= segments[mid].start)
                    left = mid + 1;
                else
                    right = mid;
            }
            for(int j = 0; j < left; j++)
                if (segments[j].stop >= point)
                    result[j]++;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort(Segment[] segments, int left, int right) {
        while (left < right) {

            int mid = left + (right - left) / 2;
            if (segments[mid].compareTo(segments[left]) < 0)
                swap(segments, left, mid);
            if (segments[right].compareTo(segments[left]) < 0)
                swap(segments, left, right);
            if (segments[right].compareTo(segments[mid]) < 0)
                swap(segments, mid, right);
            swap(segments, mid, left);
            Segment pivot = segments[left];

            int lessThanPivotEnd = left;
            int greaterThanPivotStart = right;
            int currentScanIndex = left + 1;

            while (currentScanIndex <= greaterThanPivotStart) {
                int comparisonResult = segments[currentScanIndex].compareTo(pivot);

                if (comparisonResult < 0) {
                    swap(segments, lessThanPivotEnd, currentScanIndex);
                    lessThanPivotEnd++;
                    currentScanIndex++;  // На место current пришёл элемент из зоны "==", он корректен

                } else if (comparisonResult > 0) {
                    // Элемент > pivot: отправляем в правую зону
                    swap(segments, currentScanIndex, greaterThanPivotStart);
                    greaterThanPivotStart--;

                } else {
                    currentScanIndex++;
                }
            }

            int sizeOfLessThanZone = lessThanPivotEnd - left;
            int sizeOfGreaterThanZone = right - greaterThanPivotStart;

            if (sizeOfLessThanZone < sizeOfGreaterThanZone) {
                quickSort(segments, left, lessThanPivotEnd - 1);
                left = greaterThanPivotStart + 1;
            } else {
                quickSort(segments, greaterThanPivotStart + 1, right);
                right = lessThanPivotEnd - 1;
            }
        }
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
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
            Segment other = (Segment) o;  // явное приведение типа

            // Сортируем по start, при равенстве — по stop
            if (this.start != other.start) {
                return Integer.compare(this.start, other.start);
            }
            return Integer.compare(this.stop, other.stop);
        }
    }

}
