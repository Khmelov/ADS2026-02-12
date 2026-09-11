package by.it.group510901.jalilova.lesson05;

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

    // Внутренний класс для событий
    private class Event implements Comparable<Event> {
        int time;
        int type;  // -1: старт камеры, 0: событие (точка), 1: конец работы камеры
        int index; // для сохранения исходного порядка точек

        Event(int time, int type, int index) {
            this.time = time;
            this.type = type;
            this.index = index;
        }

        @Override
        public int compareTo(Event other) {
            // Сначала сортируем по времени
            if (this.time != other.time) {
                return Integer.compare(this.time, other.time);
            }
            // Если время совпадает: СТАРТ (-1) -> ТОЧКА (0) -> КОНЕЦ (1)
            return Integer.compare(this.type, other.type);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_QSort.class.getResourceAsStream("dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        if (result != null) {
            for (int index : result) {
                System.out.print(index + " ");
            }
        }
    }

    int[] getAccessory(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        if (!scanner.hasNextInt()) return null;

        int n = scanner.nextInt(); // число отрезков
        int m = scanner.nextInt(); // число точек

        // Всего событий: 2 конца на каждый отрезок + m точек
        Event[] events = new Event[2 * n + m];
        int count = 0;

        // Читаем отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            if (start > stop) { // нормализуем отрезок
                int tmp = start; start = stop; stop = tmp;
            }
            events[count++] = new Event(start, -1, -1);
            events[count++] = new Event(stop, 1, -1);
        }

        // Читаем точки
        for (int i = 0; i < m; i++) {
            events[count++] = new Event(scanner.nextInt(), 0, i);
        }

        // Выполняем быструю сортировку событий
        quickSort(events, 0, events.length - 1);

        int[] result = new int[m];
        int activeCameras = 0;

        // Проход сканирующей прямой
        for (Event e : events) {
            if (e.type == -1) {
                activeCameras++; // Камера начала снимать
            } else if (e.type == 1) {
                activeCameras--; // Камера закончила снимать
            } else {
                result[e.index] = activeCameras; // Записываем результат для точки по её индексу
            }
        }

        return result;
    }

    // Реализация быстрой сортировки Хоара
    private void quickSort(Event[] a, int left, int right) {
        if (left >= right) return;

        int m = partition(a, left, right);
        quickSort(a, left, m);
        quickSort(a, m + 1, right);
    }

    private int partition(Event[] a, int left, int right) {
        // Опорный элемент - середина
        Event pivot = a[left + (right - left) / 2];
        int i = left;
        int j = right;

        while (i <= j) {
            while (a[i].compareTo(pivot) < 0) i++;
            while (a[j].compareTo(pivot) > 0) j--;

            if (i <= j) {
                Event temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                i++;
                j--;
            }
        }
        return j;
    }
}
