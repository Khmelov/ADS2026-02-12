package by.it.group510902.klimantovich.lesson01.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
            // Создаем отрезок, гарантируя что start <= end
            segments[i] = new Segment(Math.min(start, end), Math.max(start, end));
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        //Сортируем отрезки по началу (быстрая сортировка через Arrays.sort)
        // Для сортировки используем компаратор, реализованный в Segment.compareTo()
        Arrays.sort(segments);

        //Создаем массив концов отрезков (для бинарного поиска)
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        //Сортируем концы отрезков отдельно
        // Это нужно для эффективного подсчета количества отрезков, содержащих точку
        Arrays.sort(ends);

        //Для каждой точки определяем количество покрывающих её отрезков
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Находим количество отрезков, которые начинаются не позже точки
            // Используем бинарный поиск: ищем позицию, куда можно вставить точку
            int leftCount = findRightmostPosition(starts, point);

            // Находим количество отрезков, которые заканчиваются раньше точки
            // Отрезок НЕ покрывает точку, если его конец < точки
            int rightCount = findLeftmostPosition(ends, point);

            // Количество отрезков, покрывающих точку =
            // отрезки с началом <= точке - отрезки с концом < точке
            result[i] = leftCount - rightCount;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Вспомогательный метод: бинарный поиск последней позиции, где значение <= key
    // Возвращает количество элементов, которые <= key
    private int findRightmostPosition(int[] arr, int key) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= key) {
                result = mid;
                left = mid + 1; // Ищем дальше вправо
            } else {
                right = mid - 1;
            }
        }
        return result + 1; // +1 потому что result - индекс, а нужно количество
    }

    // Вспомогательный метод: бинарный поиск первой позиции, где значение >= key
    // Возвращает количество элементов, которые < key
    private int findLeftmostPosition(int[] arr, int key) {
        int left = 0;
        int right = arr.length - 1;
        int result = arr.length;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < key) {
                left = mid + 1;
            } else {
                result = mid;
                right = mid - 1; // Ищем дальше влево
            }
        }
        return result; // result - индекс первого элемента >= key, значит количество элементов < key = result
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            // Сравниваем отрезки по началу (start)
            // Если начала равны, то сравниваем по концу (stop)
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}