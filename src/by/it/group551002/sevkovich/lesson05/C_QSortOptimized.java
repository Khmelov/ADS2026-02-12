package by.it.group551002.sevkovich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии                !готовый
            - за сам массив отрезков - сортировка на месте                  !готовый нах
            - рекурсивные вызовы должны проводиться на основе 3-разбиения   !тоже готовый

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
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = scanner.nextInt(); // число отрезков
        int m = scanner.nextInt(); // число точек

        int[] starts = new int[n];
        int[] stops = new int[n];

        for (int i = 0; i < n; i++) {
            starts[i] = scanner.nextInt();
            stops[i] = scanner.nextInt();
        }

        Random random = new Random();

        quickSort(starts, 0, n - 1, random);
        quickSort(stops, 0, n - 1, random);

        int[] result = new int[m];
        for (int i = 0; i < m; i++) {
            int point = scanner.nextInt();

            int started = binarySearchLastLessOrEqual(starts, point);
            int endedBefore = binarySearchFirstLess(stops, point);
            result[i] = started - endedBefore;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort(int[] a, int left, int right, Random random) {
        while (left < right) {

            int pivotIdx = left + random.nextInt(right - left + 1);
            int pivot = a[pivotIdx];

            int lt = left; // a[left...lt-1] < pivot
            int i = left;  // a[lt...i-1] == pivot
            int gt = right; // a[gt+1...right] > pivot

            while (i <= gt) {
                if (a[i] < pivot) {
                    swap(a, lt++, i++);
                } else if (a[i] > pivot) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }

            // Элиминация хвостовой рекурсии: выбираем меньшую часть для рекурсии
            if (lt - left < right - gt) {
                quickSort(a, left, lt - 1, random);
                left = gt + 1;
            } else {
                quickSort(a, gt + 1, right, random);
                right = lt - 1;
            }
        }
    }

    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // Бинарный поиск: находим количество элементов <= x
    private int binarySearchLastLessOrEqual(int[] a, int x) {
        int low = 0, high = a.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (a[mid] <= x) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    // Бинарный поиск: находим количество элементов < x
    private int binarySearchFirstLess(int[] a, int x) {
        int low = 0, high = a.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (a[mid] < x) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }
}