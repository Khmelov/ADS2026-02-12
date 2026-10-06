package by.it.group510901.filimonova.lesson01.lesson05;

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
            int stop = scanner.nextInt();
            // гарантируем, что start <= stop
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp; // меняем местами
            }
            segments[i] = new Segment(start, stop);
        }

        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки по началу
        Arrays.sort(segments);

        //создаем отдельные массивы начал и концов
        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start; //starts отсортирован по возрастанию
            stops[i] = segments[i].stop; //stops не обязан быть отсортированным, но мы сохраняем соответствие индексов с starts
        }

        //считаем количество покрывающих отрезков для каждой точки
        for (int i = 0; i < m; i++) {
            int point = points[i];

            //(находим количество отрезков, у которых начало <= point)
            int leftCount = countLeft(starts, point);

            //(находим количество отрезков, у которых конец < point)
            int rightCount = countRight(stops, point);

            //кол-во отрезков, покрывающих точку = leftCount - rightCount
            result[i] = leftCount - rightCount;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //(количество элементов <= value)
    private int countLeft(int[] array, int value) {
        int left = 0; //указатель на первый эл-т, начало области поиска
        int right = array.length - 1; //указатель на последний эл-т
        int result = -1;

        while (left <= right) { //пока область не пуста, есть хотя бы один эл-т для проверки
            int mid = left + (right - left) / 2; //половина интервала
            if (array[mid] <= value) {
                result = mid; //перезаписываем
                left = mid + 1; //двигаем левую
            } else {
                right = mid - 1; //двигаем правую левее
            }
        }

        return result + 1; // количество элементов <= value
    }

    // (количество элементов < value)
    private int countRight(int[] array, int value) {
        int left = 0;
        int right = array.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (array[mid] < value) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result + 1; // количество элементов < value
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            // Сортируем по началу отрезка
            return Integer.compare(this.start, o.start);
        }
    }
}
/*1. Сортируем отрезки по началу
2. Создаем два массива: starts (все начала) и stops (все концы)
3. Для каждой точки p:
   - leftCount = количество отрезков с start <= p (бинарный поиск)
   - rightCount = количество отрезков с stop < p (бинарный поиск)
   - Результат = leftCount - rightCount */
