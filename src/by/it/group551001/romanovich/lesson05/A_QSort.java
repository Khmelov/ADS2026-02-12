package by.it.group551001.romanovich.lesson05;

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
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = segments[i].stop;
        }
        quickSort(segments, 0, n - 1);
        quickSort(stops, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int started = countStartedBeforeOrAt(segments, points[i]);
            int stopped = countStoppedBefore(stops, points[i]);
            result[i] = started - stopped;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            int byStart = Integer.compare(start, o.start);
            return byStart != 0 ? byStart : Integer.compare(stop, o.stop);
        }
    }

    private void quickSort(Segment[] array, int left, int right) {
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
        quickSort(array, left, j);
        quickSort(array, i, right);
    }

    private void quickSort(int[] array, int left, int right) {
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
        quickSort(array, left, j);
        quickSort(array, i, right);
    }

    private int countStartedBeforeOrAt(Segment[] segments, int point) {
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

    private int countStoppedBefore(int[] stops, int point) {
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

}
