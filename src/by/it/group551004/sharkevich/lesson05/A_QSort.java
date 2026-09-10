package by.it.group551004.sharkevich.lesson05;

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
        int a, b;
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
            a = scanner.nextInt();
            b = scanner.nextInt();
            segments[i] = new Segment(Math.min(a, b), Math.max(a, b));
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        Point[] sortedPoints;
        sortedPoints = new Point[m];
        for (int i = 0; i < m; i++)
            sortedPoints[i] = new Point(points[i], i);
        Arrays.sort(segments);
        Arrays.sort(sortedPoints);
        int segIndex, activeCount, endCount, end, pos;
        int[] ends;
        segIndex = 0;
        activeCount = 0;
        endCount = 0;
        ends = new int[n];

        for (Point p : sortedPoints) {
            while (segIndex < n && segments[segIndex].start <= p.value) {
                end = segments[segIndex].stop;
                pos = endCount;
                while (pos > 0 && ends[pos - 1] > end) {
                    ends[pos] = ends[pos - 1];
                    pos--;
                }
                ends[pos] = end;
                endCount++;
                segIndex++;
                activeCount++;
            }
            while (endCount > 0 && ends[0] < p.value) {
                for (int i = 0; i < endCount - 1; i++)
                    ends[i] = ends[i + 1];
                endCount--;
                activeCount--;
            }
            result[p.index] = activeCount;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
            return Integer.compare(this.start, o.start);
        }
    }

    private class Point implements Comparable<Point> {
        int value, index;

        Point(int value, int index) {
            this.value = value;
            this.index = index;
        }

        @Override
        public int compareTo(Point o) {
            return Integer.compare(this.value, o.value);
        }
    }

}
