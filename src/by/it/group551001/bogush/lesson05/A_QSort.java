package by.it.group551001.bogush.lesson05;

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
        Segment[] segments_sorted_by_start = new Segment[n];
        segments_sorted_by_start = segments.clone();
        Segment[] segments_sorted_by_stop = new Segment[n];
        segments_sorted_by_stop = segments.clone();

        QuickSort(segments_sorted_by_start,0, n - 1,1);
        QuickSort(segments_sorted_by_stop,0,n - 1,2);

        for (int i = 0; i < points.length; i++) {
            int c1 = lower_board(segments_sorted_by_start,points[i],1);
            int c2 = lower_board(segments_sorted_by_stop,points[i],2);
            result[i] = c1 - c2;
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

            return this.start - o.start;
        }

        public int comp_stop(Segment o){
            return this.stop - o.stop;
        }

    }

    void QuickSort(Segment[] segments,int left,int right,int type) {
        int mid = (left + right) / 2;
        Segment pivot = segments[mid];

        int i = left;
        int j = right;

        while (i <= j) {
            switch (type) {
                case 1:
                    while(segments[i].compareTo(pivot) < 0) {
                        i++;
                    }
                    while(segments[j].compareTo(pivot) > 0) {
                        j--;
                    }
                    break;
                case 2:
                    while(segments[i].comp_stop(pivot) < 0) {
                        i++;
                    }
                    while(segments[j].comp_stop(pivot) > 0) {
                        j--;
                    }
                    break;
            }
            if (i <= j) {
                Segment temp = segments[i];
                segments[i] = segments[j];
                segments[j] = temp;
                i++;
                j--;
            }
        }

        if (left < right)
            QuickSort(segments,left,j,type);
        if (left < right)
            QuickSort(segments,i,right,type);

    }

    int lower_board (Segment[] segments,int point,int type) {
        int left = 0;
        int right = segments.length;
        int mid = (left + right) / 2;
        switch (type) {
            case 1:
                while (left < right) {
                    if (point < segments[mid].start) {
                        right = mid;
                    } else {
                        left = mid + 1;
                    }
                    mid = (left + right) / 2;
                }
                break;
            case 2:
                while (left < right) {
                    if (point < segments[mid].stop) {
                        right = mid;
                    } else {
                        left = mid + 1;
                    }
                    mid = (left + right) / 2;
                }
                break;
        }
        return left;
    }

}
