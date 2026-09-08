package by.it.group510901.sidorov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/


public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    // Оптимизированный QuickSort с 3-разбиением и элиминацией хвоста
    private void quickSort(Segment[] a, int left, int right) {
        while (left < right) {
            // 3-way partition
            int lt = left, gt = right;
            Segment pivot = a[left];
            int i = left + 1;
            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) swap(a, lt++, i++);
                else if (cmp > 0) swap(a, i, gt--);
                else i++;
            }
            // Теперь a[left..lt-1] < pivot, a[lt..gt] == pivot, a[gt+1..right] > pivot

            // Рекурсия для меньшей части, цикл для большей (элиминация хвоста)
            if (lt - left < right - gt) {
                quickSort(a, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(a, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(Segment[] a, int i, int j) {
        Segment temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!
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

        // Сортируем отрезки
        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Бинарный поиск: ищем первый отрезок, который МОГ БЫ накрыть точку
            // (т.е. у которого start <= point)
            int l = 0, r = n - 1;
            int lastPossible = -1;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                if (segments[mid].start <= point) {
                    lastPossible = mid;
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }

            // Теперь все отрезки от 0 до lastPossible начинаются до или в точке point.
            // Но нам нужно проверить их концы!
            int count = 0;
            for (int j = 0; j <= lastPossible; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }
            result[i] = count;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Класс Segment с правильным сравнением
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Исправляем, если начало и конец перепутаны
            if (start > stop) {
                this.start = stop;
                this.stop = start;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }

        @Override
        public int compareTo(Segment o) {
            // Сортируем в первую очередь по началу отрезка
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            // Если начала равны, сортируем по концу
            return Integer.compare(this.stop, o.stop);
        }
    }

}
