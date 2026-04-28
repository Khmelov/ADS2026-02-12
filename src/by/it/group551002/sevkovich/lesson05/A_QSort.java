package by.it.group551002.sevkovich.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Random;

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

        //число точек
        int m = scanner.nextInt();
        int[] result = new int[m];
        Segment[] segments = new Segment[2*n+m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            segments[2*i] = new Segment(scanner.nextInt(), 1, 0);
            segments[2*i+1] = new Segment(scanner.nextInt(), -1, 0);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            segments[2*n+i] = new Segment(scanner.nextInt(), 0, i);
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        quickSort(segments, 0, segments.length-1, new Random());

        int activeCameras = 0;
        for (Segment segment : segments) {
            if (segment.type == 0) {
                result[segment.index] += activeCameras;
            } else {
                activeCameras += segment.type;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private void quickSort(Segment[] segments, int left, int right, Random random) {
        if (left>=right) return;
        int pivotIdx = random.nextInt(left, right);
        Segment pivot = segments[pivotIdx];
        int i = left;
        int j = right-1;
        while(true) {
            while (segments[i].compareTo(pivot) < 0) {
                i++;
            }
            while (segments[j].compareTo(pivot) > 0) {
                j--;
            }
            if (i>=j) {
                break;
            }
            Segment temp = segments[i];
            segments[i] = segments[j];
            segments[j] = temp;
        }

        quickSort(segments, left, j, random);
        quickSort(segments, j+1, right, random);
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int time;
        int type; // 1 - start, 0 - point, -1 - end
        int index;

        Segment(int time, int type, int index) {
            this.time = time;
            this.type = type;
            this.index = index;
        }

        @Override
        public int compareTo(Segment o) {
            if (this.time > o.time) {
                return 1;
            }
            else if (this.time == o.time)
                return Integer.compare(this.type, o.type);
            else return -1;
        }
    }

}
