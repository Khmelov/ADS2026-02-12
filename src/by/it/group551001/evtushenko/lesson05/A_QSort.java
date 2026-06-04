package by.it.group551001.evtushenko.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.Segment;

import javafx.scene.effect.Light.Point;

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

        Pair[] combined = new Pair[segments.length * 2 + points.length];
        
        for(int i = 0; i < points.length; ++i) combined[i] = new Pair(points[i], 0, i);
        for(int i = 0; i < segments.length; ++i){
            combined[points.length + i] = new Pair(segments[i].start, -1, 0);
            combined[combined.length - i - 1] = new Pair(segments[i].stop, 1, 0);
        }

        qSort(combined, 0, combined.length - 1);

        for(int i = 0, j = 0, c = 0; i < points.length; ++j){
            if(combined[j].b == -1){
                ++c;
                continue;
            }

            if(combined[j].b == 1){
                --c;
                continue;
            }

            result[combined[j].i] = c;
            ++i;
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
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(start, o.start);
        }
    }

    private class Pair implements Comparable<Pair> {
        int a;
        int b;
        int i;

        Pair(int a, int b, int i) {
            this.a = a;
            this.b = b;
            this.i = i;
        }

        @Override
        public int compareTo(Pair o) {
            if(a == o.a) return Integer.compare(b, o.b);

            return Integer.compare(a, o.a);
        }
    }

    <T extends Comparable<T>> void qSort(T[] a, int b, int e){
        T m = a[(b + e) / 2];

        int i = b, j = e;
        while(i <= j){
            while(a[i].compareTo(m) < 0)
                ++i;
            while(a[j].compareTo(m) > 0)
                --j;

            if(i <= j) {
                T t = a[i];
                a[i] = a[j];
                a[j] = t;
                i++;
                --j;
            }
        }

        if(b < j) qSort(a, b, j);
        if(i < e) qSort(a, i, e);
    }

}
