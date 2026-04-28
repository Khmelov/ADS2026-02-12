package by.it.group551001.bolbas.lesson05;

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
            int start = scanner.nextInt();
            int end = scanner.nextInt();
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

        Arrays.sort(segments);

        Event[] events = new Event[2 * n];
        for (int i = 0; i < n; i++) {
            events[2 * i] = new Event(segments[i].start, true);  // start event
            events[2 * i + 1] = new Event(segments[i].stop, false); // end event
        }

        Arrays.sort(events, (e1, e2) -> {
            if (e1.coordinate != e2.coordinate) {
                return Integer.compare(e1.coordinate, e2.coordinate);
            }
            // Start events come before end events at the same coordinate
            if (e1.isStart && !e2.isStart) return -1;
            if (!e1.isStart && e2.isStart) return 1;
            return 0;
        });

        Point[] pointObjects = new Point[m];
        for (int i = 0; i < m; i++) {
            pointObjects[i] = new Point(points[i], i);
        }

        Arrays.sort(pointObjects, (p1, p2) -> Integer.compare(p1.coordinate, p2.coordinate));

        int activeCount = 0;
        int eventIndex = 0;

        for (Point point : pointObjects) {
            while (eventIndex < events.length && events[eventIndex].coordinate <= point.coordinate) {
                if (events[eventIndex].isStart) {
                    activeCount++;
                } else {
                    activeCount--;
                }
                eventIndex++;
            }
            result[point.originalIndex] = activeCount;
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
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }
    private class Event {
        int coordinate;
        boolean isStart;

        Event(int coordinate, boolean isStart) {
            this.coordinate = coordinate;
            this.isStart = isStart;
        }
    }

    private class Point {
        int coordinate;
        int originalIndex;

        Point(int coordinate, int originalIndex) {
            this.coordinate = coordinate;
            this.originalIndex = originalIndex;
        }
    }
}
