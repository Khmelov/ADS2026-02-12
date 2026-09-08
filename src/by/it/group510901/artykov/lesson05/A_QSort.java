package by.it.group510901.artykov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 * Видеорегистраторы и события.
 *
 * Даны:
 * 1. Отрезки работы камер
 * 2. Точки событий
 *
 * Для каждой точки необходимо определить,
 * сколько отрезков её содержат.
 *
 * Используется:
 * - быстрая сортировка
 * - компаратор для отрезков
 */

public class A_QSort {

    public static void main(String[] args)
            throws FileNotFoundException {

        InputStream stream =
                A_QSort.class.getResourceAsStream("dataA.txt");

        A_QSort instance = new A_QSort();

        int[] result = instance.getAccessory(stream);

        // Вывод результата
        for (int index : result) {

            System.out.print(index + " ");
        }
    }

    /*
     * Основной метод решения задачи.
     */
    int[] getAccessory(InputStream stream)
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
             * Проверяем все отрезки
             */
            for (Segment segment : segments) {

                /*
                 * Если точка принадлежит отрезку
                 */
                if (point >= segment.start
                        && point <= segment.stop) {

                    count++;
                }
            }

            result[i] = count;
        }

        return result;
    }

    /*
     * Быстрая сортировка массива.
     */
    void quickSort(Segment[] arr, int left, int right) {

        if (left >= right) {

            return;
        }

        // Опорный элемент
        Segment pivot =
                arr[(left + right) / 2];

        int i = left;
        int j = right;

        while (i <= j) {

            while (arr[i].compareTo(pivot) < 0) {

                i++;
            }

            while (arr[j].compareTo(pivot) > 0) {

                j--;
            }

            if (i <= j) {

                // Обмен элементов
                Segment temp = arr[i];

                arr[i] = arr[j];

                arr[j] = temp;

                i++;
                j--;
            }
        }

        // Сортировка левой части
        quickSort(arr, left, j);

        // Сортировка правой части
        quickSort(arr, i, right);
    }

    // =========================================================
    // Класс отрезка
    // =========================================================

    private class Segment
            implements Comparable<Segment> {

        int start;
        int stop;

        /*
         * Конструктор отрезка.
         */
        Segment(int start, int stop) {

            /*
             * Если границы перепутаны,
             * меняем их местами
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
         * Сначала сравниваются начала,
         * затем концы.
         */
        @Override
        public int compareTo(Segment o) {

            // Сравнение начала
            if (this.start != o.start) {

                return Integer.compare(
                        this.start,
                        o.start
                );
            }

            // Сравнение конца
            return Integer.compare(
                    this.stop,
                    o.stop
            );
        }
    }
}