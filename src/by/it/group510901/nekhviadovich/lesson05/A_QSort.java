package by.it.group510901.nekhviadovich.lesson05;

import by.it.group510901.nekhviadovich.lesson02.C_GreedyKnapsack;

import javax.swing.text.Segment;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
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

        // сортируем отрезки твоей быстрой сортировкой
        quickSort(segments, 0, segments.length - 1);

        // для каждой точки считаем количество покрывающих отрезков
        for (int i = 0; i < m; i++) {
            int count = 0;
            // проходим по всем отрезкам и считаем те, которые покрывают точку
            for (int j = 0; j < n; j++) {
                if (segments[j].start <= points[i] && points[i] <= segments[j].stop) {
                    count++;
                }
                // если отрезок начинается после точки, дальше можно не проверять (они отсортированы)
                if (segments[j].start > points[i]) {
                    break;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    private static void quickSort(Segment[] items, int startBorder, int endBorder){
        if(endBorder <= startBorder) return;
        int pivot = startBorder;
        int left = startBorder + 1;
        int right = endBorder;

        while (left <= right) {
            while(left <= right && items[pivot].compareTo(items[left]) >= 0){
                left++;
            }
            while(left <= right && right > startBorder && items[pivot].compareTo(items[right]) <= 0 ){
                right--;
            }
            if(left < right){
                Segment temp = items[left];
                items[left] = items[right];
                items[right] = temp;
            }
        }
        Segment temp = items[pivot];
        items[pivot] = items[right];
        items[right] = temp;
        pivot = right;

        quickSort(items, startBorder, pivot - 1 );
        quickSort(items, pivot + 1, endBorder);
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {

            if(start < stop){
                this.start = start;
                this.stop = stop;
            }
            else{
                this.start = stop;
                this.stop = start;
            }
            //тут вообще-то лучше доделать конструктор на случай если
            //концы отрезков придут в обратном порядке
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            if(this.start > o.start) return 1;
            else if(this.start == o.start) return 0;
            else return -1;
        }
    }

}
