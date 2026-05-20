package by.it.group510901.chernyavskaya.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
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
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            // если конец меньше начала — меняем местами
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }
            segments[i] = new Segment(start, end);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        // Шаг 1: Сортируем отрезки по началу используя быструю сортировку
        quickSortSegments(segments, 0, segments.length - 1);

        // Шаг 2: Для каждой точки считаем количество отрезков, которым она принадлежит
        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // так как отрезки отсортированы по началу, можно остановиться когда начало отрезка > точки
            for (int j = 0; j < n; j++) {
                // Если начало текущего отрезка больше точки, дальше можно не проверять
                // (так как отрезки отсортированы по возрастанию начала)
                if (segments[j].start > point) {
                    break;
                }
                // Проверяем, принадлежит ли точка отрезку (включая границы)
                if (point >= segments[j].start && point <= segments[j].stop) {
                    count++;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Реализация быстрой сортировки для массива отрезков
    private void quickSortSegments(Segment[] arr, int left, int right) {
        if (left < right) {
            // Находим опорный элемент и разбиваем массив
            int pivotIndex = partition(arr, left, right);
            // Рекурсивно сортируем левую и правую части
            quickSortSegments(arr, left, pivotIndex - 1);
            quickSortSegments(arr, pivotIndex + 1, right);
        }
    }

    // Функция разделения массива для быстрой сортировки
    private int partition(Segment[] arr, int left, int right) {
        // Выбираем опорный элемент - средний элемент для улучшения производительности
        int mid = left + (right - left) / 2;
        Segment pivot = arr[mid];

        // Меняем местами опорный элемент с последним
        Segment temp = arr[mid];
        arr[mid] = arr[right];
        arr[right] = temp;

        int i = left;

        // Перемещаем все элементы, меньшие опорного, в левую часть
        for (int j = left; j < right; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                // Меняем местами arr[i] и arr[j]
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
            }
        }

        // Возвращаем опорный элемент на его место
        temp = arr[i];
        arr[i] = arr[right];
        arr[right] = temp;

        return i;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // Сравниваем отрезки по началу (start)
            // Если начала равны, то сравниваем по концу (stop)
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            } else {
                return Integer.compare(this.stop, o.stop);
            }
        }
    }

}