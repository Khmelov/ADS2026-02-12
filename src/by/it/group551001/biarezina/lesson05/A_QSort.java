package by.it.group551001.biarezina.lesson05;

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

    private static int partition(Segment[] a, int l, int r) {
        Segment x = a[l];
        int j = l;

        for (int i = l + 1; i <= r; i++) {
            if (x.compareTo(a[i]) != -1) {
                j++;
                Segment temp = a[j];
                a[j] = a[i];
                a[i] = temp;
            }
        }

        Segment temp = a[l];
        a[l] = a[j];
        a[j] = temp;

        return j;
    }


    public static void quickSort(Segment[] a, int l, int r) {
        if (l >= r) {
            return;
        }

        int m = partition(a, l, r);

        quickSort(a, l, m - 1);

        quickSort(a, m + 1, r);
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

        quickSort(segments, 0, n-1);
        for (int i = 0; i < n; i++){
            System.out.println(segments[i].start + ", " + segments[i].stop + "\n");
        }

        for (int j = 0; j < m; j++){
            int i = 0;

            while (i < n){
                if (segments[i].start <= points[j] && points[j] <= segments[i].stop){
                    result[j]++;
                }
                if (segments[i].start > points[j]) {
                    break;
                }
                i++;
            }
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор


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
            if (this.start < o.start){
                return -1;
            } else if (this.start > o.start){
                return 1;
            } else if (this.stop < o.stop){
                return -1;
            } else if (this.stop > o.stop) {
                return 1;
            }
            //подумайте, что должен возвращать компаратор отрезков

            return 0;
        }
    }

}
