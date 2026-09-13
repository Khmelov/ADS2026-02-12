package by.it.group510902.yantsukevich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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

    private static void quickSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int left, int right) {
        while (left < right) {
            int[] bounds = threeWayPartition(arr, left, right);
            int lt = bounds[0];
            int gt = bounds[1];
            // Рекурсия на меньшей части, итерация на большей
            if (lt - left < right - gt) {
                quickSort(arr, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(arr, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    // 3-разбиение (голландский флаг)
    private static int[] threeWayPartition(int[] arr, int left, int right) {
        int pivot = medianOfThree(arr, left, right);
        // Найдём позицию pivot и обменяем с left
        int pivotPos = findPivotPosition(arr, left, right, pivot);
        swap(arr, left, pivotPos);

        int i = left;
        int low = left;
        int high = right;
        while (i <= high) {
            if (arr[i] < pivot) {
                swap(arr, i, low);
                i++;
                low++;
            } else if (arr[i] > pivot) {
                swap(arr, i, high);
                high--;
            } else {
                i++;
            }
        }
        return new int[]{low, high};
    }

    private static int medianOfThree(int[] arr, int left, int right) {
        int mid = left + (right - left) / 2;
        int a = arr[left];
        int b = arr[mid];
        int c = arr[right];
        if ((a <= b && b <= c) || (c <= b && b <= a)) return b;
        if ((b <= a && a <= c) || (c <= a && a <= b)) return a;
        return c;
    }

    private static int findPivotPosition(int[] arr, int left, int right, int pivot) {
        for (int i = left; i <= right; i++) {
            if (arr[i] == pivot) return i;
        }
        return left;
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
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


        int[] st = new int[n];
        int[] fn = new int[n];

        for(int i = 0; i < n; i++){
            st[i] = segments[i].start;
            fn[i] = segments[i].stop + 1;
        }

        quickSort(st);
        quickSort(fn);

        int uks = 0, ukf = 0;
        for(int i = 0; i < m; i++){
            while(uks < n && st[uks] < points[i]) {
                uks++;
            }
            while(ukf < n && fn[ukf] <= points[i]) {
                ukf++;
            }
            result[i] = uks - ukf;
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

}
