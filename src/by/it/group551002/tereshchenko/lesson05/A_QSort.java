package by.it.group551002.tereshchenko.lesson05;

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
        int[] stops = new int[n];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
            stops[i] = segments[i].stop;
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        quickSortSegments(segments, 0, n - 1);
        quickSortInt(stops, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];

            int started = countStartsLessOrEqual(segments, point);
            int finished = countStopsLess(stops, point);

            result[i] = started - finished;
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int countStartsLessOrEqual(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length;

        while (left < right) {
            int middle = left + (right - left) / 2;

            if (segments[middle].start <= point) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }
    int countStopsLess(int[] stops, int point) {
        int left = 0;
        int right = stops.length;

        while (left < right) {
            int middle = left + (right - left) / 2;

            if (stops[middle] < point) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }

    void quickSortSegments(Segment[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        int i = left;
        int j = right;

        Segment pivot = array[left + (right - left) / 2];

        while (i <= j) {
            while (array[i].compareTo(pivot) < 0) {
                i++;
            }

            while (array[j].compareTo(pivot) > 0) {
                j--;
            }

            if (i <= j) {
                Segment temp = array[i];
                array[i] = array[j];
                array[j] = temp;

                i++;
                j--;
            }
        }

        if (left < j) {
            quickSortSegments(array, left, j);
        }

        if (i < right) {
            quickSortSegments(array, i, right);
        }
    }

    void quickSortInt(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        int i = left;
        int j = right;

        int pivot = array[left + (right - left) / 2];

        while (i <= j) {
            while (array[i] < pivot) {
                i++;
            }

            while (array[j] > pivot) {
                j--;
            }

            if (i <= j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;

                i++;
                j--;
            }
        }

        if (left < j) {
            quickSortInt(array, left, j);
        }

        if (i < right) {
            quickSortInt(array, i, right);
        }
    }
    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start != o.start) {
                return this.start - o.start;
            }
            return this.stop - o.stop;
            //подумайте, что должен возвращать компаратор отрезков
        }
    }

}
