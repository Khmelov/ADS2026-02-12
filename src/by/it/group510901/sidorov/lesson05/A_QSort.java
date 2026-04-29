package by.it.group510901.sidorov.lesson05;

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

    private class Event implements Comparable<Event> {
        int x;      // координата
        int type;   // тип: -1 (начало), 0 (точка), 1 (конец)
        int index;  // индекс для точек, чтобы вернуть ответ в нужном порядке

        Event(int x, int type, int index) {
            this.x = x;
            this.type = type;
            this.index = index;
        }

        @Override
        public int compareTo(Event other) {
            // Сначала сортируем по координате
            if (this.x != other.x) {
                return Integer.compare(this.x, other.x);
            }
            // Если координаты равны, сначала Начало (-1), потом Точка (0), потом Конец (1)
            return Integer.compare(this.type, other.type);
        }
    }

    // Быстрая Сортировка (QuickSort)
    private void quickSort(Event[] events, int left, int right) {
        if (left < right) {
            // Разделяем массив и получаем индекс опорного элемента
            int pivotIndex = partition(events, left, right);
            // Рекурсивно сортируем левую и правую части
            quickSort(events, left, pivotIndex);
            quickSort(events, pivotIndex + 1, right);
        }
    }

    private int partition(Event[] events, int left, int right) {
        // Выбираем опорный элемент (pivot) - берем средний, чтобы избежать O(n^2) на отсортированных данных
        Event pivot = events[left + (right - left) / 2];
        int i = left;
        int j = right;

        while (true) {
            // Ищем элемент слева, который должен стоять справа от pivot
            while (events[i].compareTo(pivot) < 0) i++;
            // Ищем элемент справа, который должен стоять слева от pivot
            while (events[j].compareTo(pivot) > 0) j--;

            if (i >= j) return j;

            // Меняем их местами
            Event temp = events[i];
            events[i] = events[j];
            events[j] = temp;

            i++;
            j--;
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
        Event[] events = new Event[2 * n + m];
        int eventIdx = 0;

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            // Гарантируем, что start <= stop
            if (start > stop) {
                int temp = start; start = stop; stop = temp;
            }
            events[eventIdx++] = new Event(start, -1, -1);
            events[eventIdx++] = new Event(stop, 1, -1);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            int x = scanner.nextInt();
            events[eventIdx++] = new Event(x, 0, i);
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор
        quickSort(events, 0, events.length - 1);

        int[] result = new int[m];
        int activeSegments = 0;

        // Проход сканирующей прямой
        for (Event e : events) {
            if (e.type == -1) {
                activeSegments++; // Камера включилась
            } else if (e.type == 1) {
                activeSegments--; // Камера выключилась
            } else {
                // Это точка, сохраняем текущее кол-во камер
                result[e.index] = activeSegments;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}
