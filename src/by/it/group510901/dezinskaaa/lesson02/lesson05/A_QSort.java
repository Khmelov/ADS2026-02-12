package by.it.group510901.dezinskaaa.lesson02.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_QSort {

    public static void main(String[] args) throws FileNotFoundException {
        // Открываем поток для чтения файла dataA.txt из ресурсов
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();               // Создаём экземпляр класса
        int[] result = instance.getAccessory(stream);   // Вычисляем результат для всех точек
        for (int index : result) {                      // Выводим результат через пробел
            System.out.print(index + " ");
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];

        // читаем сами отрезки
        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }
        // читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // создаём отдельные массивы начал и концов отрезков для быстрой сортировки
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;   // заполняем начала
            ends[i] = segments[i].stop;      // заполняем концы
        }


        quickSort(starts, 0, n - 1);
        quickSort(ends, 0, n - 1);


        for (int i = 0; i < m; i++) {
            int point = points[i];
            int cntStart = upperBound(starts, point);
            int cntEnd = lowerBound(ends, point);
            result[i] = cntStart - cntEnd;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Быстрая сортировка для массива int (рекурсивная)
    private void quickSort(int[] arr, int left, int right) {
        if (left < right) {                                // если в подмассиве больше одного элемента
            int pivotIndex = partition(arr, left, right); // делим массив на две части
            quickSort(arr, left, pivotIndex - 1);         // рекурсивно сортируем левую часть
            quickSort(arr, pivotIndex + 1, right);        // рекурсивно сортируем правую часть
        }
    }

    // Разбиение Хоара для быстрой сортировки
    private int partition(int[] arr, int left, int right) {
        // опорный элемент – средний по индексу
        int pivot = arr[(left + right) / 2];
        int i = left;      // указатель слева
        int j = right;     // указатель справа
        while (i <= j) {   // пока границы не пересеклись
            // двигаем i вправо, пока arr[i] < pivot
            while (arr[i] < pivot) i++;
            // двигаем j влево, пока arr[j] > pivot
            while (arr[j] > pivot) j--;
            if (i <= j) {            // если найден обмен
                int temp = arr[i];   // меняем местами
                arr[i] = arr[j];
                arr[j] = temp;
                i++;                 // сдвигаем указатели
                j--;
            }
        }
        return i;  // индекс, с которого начинается правая часть
    }

    // Возвращает количество элементов массива arr, которые <= x (upper bound)
    private int upperBound(int[] arr, int x) {
        int left = 0, right = arr.length;
        while (left < right) {                 // бинарный поиск
            int mid = (left + right) / 2;
            if (arr[mid] <= x) {               // если середина <= x
                left = mid + 1;                // ищем правее
            } else {
                right = mid;                   // ищем левее
            }
        }
        return left; // количество элементов <= x
    }

    // Возвращает количество элементов массива arr, которые < x (lower bound)
    private int lowerBound(int[] arr, int x) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = (left + right) / 2;
            if (arr[mid] < x) {                // если середина < x
                left = mid + 1;                // ищем правее
            } else {
                right = mid;                   // ищем левее
            }
        }
        return left; // количество элементов < x
    }

    // Внутренний класс, описывающий отрезок [start, stop]
    private class Segment implements Comparable<Segment> {
        int start;  // начало отрезка
        int stop;   // конец отрезка

        Segment(int start, int stop) {
            // Обеспечиваем корректный порядок концов (start <= stop)
            if (start <= stop) {
                this.start = start;
                this.stop = stop;
            } else {
                this.start = stop;
                this.stop = start;
            }
        }

        @Override
        public int compareTo(Segment o) {
            // Сравнение отрезков по началу (для возможной сортировки, хотя в этом решении не используется)
            return Integer.compare(this.start, o.start);
        }
    }
}