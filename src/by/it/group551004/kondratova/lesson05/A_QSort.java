package by.it.group551004.kondratova.lesson05;

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
    Шаг Описание
1 Читаем все отрезки [start, stop]
2 Сортируем отрезки по началу (start)
3 Создаём два массива: starts (все начала) и stops (все концы)
4 Сортируем stops отдельно (для бинарного поиска)
5 Для каждой точки point считаем:
результат = (количество отрезков с start <= point) - (количество отрезков с stop < point)
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
        //число отрезков
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        //число точек
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            //если концы пришли в обратном порядке, меняем их местами
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            segments[i] = new Segment(start, stop);
        }

        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по началу
        Arrays.sort(segments);

        // Создаем массивы начал и концов для бинарного поиска
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }

        // Сортируем концы отдельно для бинарного поиска
        Arrays.sort(stops);

        // Для каждой точки считаем количество отрезков, которым она принадлежит
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Количество отрезков, у которых начало <= point
            int leftCount = countLessOrEqual(starts, point);

            // Количество отрезков, у которых конец < point (не принадлежат)
            int rightCount = countLess(stops, point);

            // Точка принадлежит отрезку, если начало <= point <= конец
            // То есть: отрезки с началом <= point минус отрезки с концом < point
            result[i] = leftCount - rightCount;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Бинарный поиск: количество элементов <= x
    private int countLessOrEqual(int[] arr, int x) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= x) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result + 1;
    }

    // Бинарный поиск: количество элементов < x
    private int countLess(int[] arr, int x) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] < x) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result + 1;
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
            // Сортируем по началу отрезка, если равны - по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
}