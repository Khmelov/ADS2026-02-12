package by.it.group510902.vinnichuk.lesson05;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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
        //создаю массив всех точек для сортировки
        Point[] allPoints = new Point[2 * n + m];
        int index = 0;

        // записываю отрезки
        for (Segment segment : segments) {
            allPoints[index++] = new Point(segment.start, -1, 0);
            allPoints[index++] = new Point(segment.stop, 1, 0);
        }

        // записываю точки
        for (int i = 0; i < m; i++) {
            allPoints[index++] = new Point(points[i], 0, i);
        }

        // сортирую быстрой сортировкой
        quickSort(allPoints, 0, allPoints.length - 1);

        // считаю покрытие камер для точек
        int activeCameras = 0;
        for (Point p : allPoints) {
            if (p.type == -1) {
                activeCameras++;
            } else if (p.type == 1) {
                activeCameras--;
            } else {
                result[p.originalIndex] = activeCameras;
            }
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // класс для событий сканирующей прямой
    private class Point implements Comparable<Point> {
        int x;
        int type; // -1 старт, 0 точка, 1 стоп
        int originalIndex;

        Point(int x, int type, int originalIndex) {
            this.x = x;
            this.type = type;
            this.originalIndex = originalIndex;
        }

        @Override
        public int compareTo(Point other) {
            if (this.x != other.x) {
                return Integer.compare(this.x, other.x);
            }
            return Integer.compare(this.type, other.type);
        }
    }

    // реализация быстрой сортировки
    private void quickSort(Point[] a, int left, int right) {
        if (left >= right) return;
        int m = partition(a, left, right);
        quickSort(a, left, m - 1);
        quickSort(a, m + 1, right);
    }

    // разбиение для qsort
    private int partition(Point[] a, int left, int right) {
        Point pivot = a[left];
        int j = left;
        for (int i = left + 1; i <= right; i++) {
            if (a[i].compareTo(pivot) <= 0) {
                j++;
                Point temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        Point temp = a[left];
        a[left] = a[j];
        a[j] = temp;
        return j;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // проверяю порядок концов
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            // сравниваю по началу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

}
