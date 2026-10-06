package by.it.group510901.artykov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Видеорегистраторы и события.
 *
 * Даны:
 * 1. Отрезки работы камер
 * 2. Моменты событий
 *
 * Для каждой точки нужно определить,
 * сколько отрезков её содержат.
 *
 * Используется:
 * - оптимизированная быстрая сортировка
 * - 3-разбиение
 * - бинарный поиск
 */

public class C_QSortOptimized {

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                C_QSortOptimized.class.getResourceAsStream("dataC.txt");

        C_QSortOptimized instance =
                new C_QSortOptimized();

        int[] result =
                instance.getAccessory2(stream);

        // Вывод результата
        for (int index : result) {

            System.out.print(index + " ");
        }
    }

    /*
     * Основной метод решения задачи.
     */
    int[] getAccessory2(InputStream stream)
            throws FileNotFoundException {

        // Чтение данных
        Scanner scanner = new Scanner(stream);

        // Количество отрезков
        int n = scanner.nextInt();

        Segment[] segments = new Segment[n];

        // Количество точек
        int m = scanner.nextInt();

        int[] points = new int[m];

        int[] result = new int[m];

        // =====================================================
        // Чтение отрезков
        // =====================================================

        for (int i = 0; i < n; i++) {

            segments[i] =
                    new Segment(
                            scanner.nextInt(),
                            scanner.nextInt()
                    );
        }

        // =====================================================
        // Чтение точек
        // =====================================================

        for (int i = 0; i < m; i++) {

            points[i] = scanner.nextInt();
        }

        // =====================================================
        // Сортировка отрезков
        // =====================================================

        quickSort(segments, 0, n - 1);

        // =====================================================
        // Проверка точек
        // =====================================================

        for (int i = 0; i < m; i++) {

            int point = points[i];

            int count = 0;

            /*
             * Бинарный поиск первого
             * возможного отрезка
             */
            int left = 0;
            int right = n - 1;

            int found = -1;

            while (left <= right) {

                int mid = (left + right) / 2;

                if (segments[mid].start <= point) {

                    found = mid;

                    left = mid + 1;

                } else {

                    right = mid - 1;
                }
            }

            /*
             * Проверяем подходящие отрезки
             * слева от найденного
             */
            for (int j = found; j >= 0; j--) {

                if (segments[j].start <= point
                        && segments[j].stop >= point) {

                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    /*
     * Оптимизированная быстрая сортировка.
     *
     * Используется 3-разбиение.
     */
    void quickSort(Segment[] arr, int left, int right) {

        while (left < right) {

            Segment pivot =
                    arr[(left + right) / 2];

            int lt = left;
            int gt = right;

            int i = left;

            /*
             * 3-разбиение:
             * < pivot
             * = pivot
             * > pivot
             */
            while (i <= gt) {

                int compare =
                        arr[i].compareTo(pivot);

                if (compare < 0) {

                    swap(arr, lt, i);

                    lt++;
                    i++;

                } else if (compare > 0) {

                    swap(arr, i, gt);

                    gt--;

                } else {

                    i++;
                }
            }

            /*
             * Рекурсия для меньшей части
             * (оптимизация стека)
             */
            if (lt - left < right - gt) {

                quickSort(arr, left, lt - 1);

                left = gt + 1;

            } else {

                quickSort(arr, gt + 1, right);

                right = lt - 1;
            }
        }
    }

    /*
     * Обмен элементов местами.
     */
    void swap(Segment[] arr, int i, int j) {

        Segment temp = arr[i];

        arr[i] = arr[j];

        arr[j] = temp;
    }

    // =========================================================
    // Класс отрезка
    // =========================================================

    private class Segment
            implements Comparable {

        int start;
        int stop;

        /*
         * Конструктор отрезка.
         */
        Segment(int start, int stop) {

            /*
             * Если границы перепутаны,
             * меняем местами
             */
            if (start <= stop) {

                this.start = start;
                this.stop = stop;

            } else {

                this.start = stop;
                this.stop = start;
            }
        }

        /*
         * Сравнение отрезков.
         *
         * Сначала по началу,
         * потом по концу.
         */
        @Override
        public int compareTo(Object o) {

            Segment other = (Segment) o;

            if (this.start != other.start) {

                return Integer.compare(
                        this.start,
                        other.start
                );
            }

            return Integer.compare(
                    this.stop,
                    other.stop
            );
        }
    }
}