package by.it.group510902.gordeev.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    // Оптимизированная версия: использует быструю сортировку с 3-частным разбиением (3-way partitioning)
    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();          // количество отрезков
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();          // количество точек
        int[] points = new int[m];
        int[] result = new int[m];

        // Читаем отрезки
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        // Читаем точки
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

        // Вытаскиваем начала и концы в отдельные массивы
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Сортируем с помощью 3-way QuickSort (оптимизация для повторяющихся элементов)
        quickSort3Way(starts, 0, n - 1);
        quickSort3Way(ends, 0, n - 1);

        // Классический подсчёт через upperBound и lowerBound
        for (int i = 0; i < m; i++) {
            int p = points[i];
            int countStart = upperBound(starts, p);   // кол-во start <= p
            int countEnd = lowerBound(ends, p);       // кол-во end < p
        quickSort3Way(starts, 0, n - 1);
        quickSort3Way(ends, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int p = points[i];
            int countStart = upperBound(starts, p);
            int countEnd = lowerBound(ends, p);
            result[i] = countStart - countEnd;
        }

        return result;
    }

    // QuickSort с трёхчастным разбиением (3-way partitioning)
    // Эффективен когда много повторяющихся элементов
    // Разбивает массив на 3 части: < pivot, == pivot, > pivot
    private void quickSort3Way(int[] arr, int low, int high) {
        while (low < high) {
            int lt = low;           // граница элементов < pivot
            int gt = high;          // граница элементов > pivot
            int pivot = arr[low + (high - low) / 2];  // опорный элемент (из середины)
            int i = low;

            // Проход по массиву, распределение по трём областям
            while (i <= gt) {
                if (arr[i] < pivot) {
                    swap(arr, lt++, i++);   // элемент меньше pivot → в левую часть
                } else if (arr[i] > pivot) {
                    swap(arr, i, gt--);     // элемент больше pivot → в правую часть
                } else {
                    i++;                     // элемент равен pivot → оставляем в середине
                }
            }

            // Рекурсивная оптимизация: сортируем меньшую часть, а большую обрабатываем в цикле
            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);  // сортируем левую часть
                low = gt + 1;                      // затем правую
            } else {
                quickSort3Way(arr, gt + 1, high);  // сортируем правую часть
                high = lt - 1;                     // затем левую
            }
        }
    }

    // Меняет местами два элемента в массиве
    private void quickSort3Way(int[] arr, int low, int high) {
        while (low < high) {
            int lt = low, gt = high;
            int pivot = arr[low + (high - low) / 2];
            int i = low;

            while (i <= gt) {
                if (arr[i] < pivot) {
                    swap(arr, lt++, i++);
                } else if (arr[i] > pivot) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }

            if (lt - low < high - gt) {
                quickSort3Way(arr, low, lt - 1);
                low = gt + 1;
            } else {
                quickSort3Way(arr, gt + 1, high);
                high = lt - 1;
            }
        }
    }

    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // Бинарный поиск: возвращает кол-во элементов <= key
    private int upperBound(int[] arr, int key) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= key) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    // Бинарный поиск: возвращает кол-во элементов < key
    private int lowerBound(int[] arr, int key) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < key) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    // Класс отрезка (реализует Comparable для возможной сортировки, но в коде не используется)
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}