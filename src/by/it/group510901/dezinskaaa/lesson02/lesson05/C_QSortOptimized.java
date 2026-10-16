package by.it.group510901.dezinskaaa.lesson02.lesson05;

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
*/

public class C_QSortOptimized {

    public static void main(String[] args) throws FileNotFoundException {
        // Открываем поток для чтения файла dataC.txt из ресурсов
        InputStream stream = C_QSortOptimized.class.getResourceAsStream("dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();   // Создаём экземпляр класса
        int[] result = instance.getAccessory2(stream);        // Получаем результат (массив ответов)
        for (int index : result) {                            // Выводим результаты через пробел
            System.out.print(index + " ");
        }
    }

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        // Создаём сканер для чтения из входного потока
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];


        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        quickSort3Way(segments, 0, n - 1);

        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            ends[i] = segments[i].stop;
        }
        quickSort3Way(ends, 0, n - 1);

         for (int i = 0; i < m; i++) {
            int p = points[i];
            int cntStart = upperBoundByStart(segments, p); // сколько отрезков с start <= p
            int cntEnd = lowerBound(ends, p);              // сколько отрезков с stop < p
            result[i] = cntStart - cntEnd;                 // покрывающие отрезки
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // ------------------------------------------------------------
    // 3‑разбиение и быстрая сортировка для массива отрезков
    // ------------------------------------------------------------
    private void quickSort3Way(Segment[] a, int lo, int hi) {
        // Элиминация хвостовой рекурсии: обрабатываем бóльшую часть итеративно,
        // а меньшую отправляем в рекурсию. Это позволяет избежать переполнения стека.
        while (lo < hi) {
            // 3‑разбиение: [lo .. lt-1] < pivot, [lt .. gt] == pivot, [gt+1 .. hi] > pivot
            int lt = lo;      // левая граница равных элементов
            int gt = hi;      // правая граница равных элементов
            int i = lo;       // текущий индекс
            Segment pivot = a[lo + (hi - lo) / 2]; // опорный элемент (середина)

            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if (cmp < 0) {
                    // текущий элемент меньше опорного – меняем с левой границей
                    swap(a, i, lt);
                    i++;
                    lt++;
                } else if (cmp > 0) {
                    // текущий элемент больше опорного – меняем с правой границей
                    swap(a, i, gt);
                    gt--;
                } else {
                    // равны – просто двигаем указатель
                    i++;
                }
            }
            // Теперь lt - первый индекс после равных, gt - последний индекс равных.
            // Рекурсивно сортируем левую часть (меньшие) и правую часть (большие).
            // Выбираем меньшую часть для рекурсии, большую обрабатываем в цикле.
            if (lt - 1 - lo < hi - (gt + 1)) {
                // Левая часть меньше – рекурсивно сортируем её
                quickSort3Way(a, lo, lt - 1);
                lo = gt + 1;   // следующая итерация цикла – правая часть
            } else {
                // Правая часть меньше – рекурсивно сортируем её
                quickSort3Way(a, gt + 1, hi);
                hi = lt - 1;   // следующая итерация цикла – левая часть
            }
        }
    }

    // Обмен двух отрезков в массиве
    private void swap(Segment[] a, int i, int j) {
        Segment t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // ------------------------------------------------------------
    // 3‑разбиение и быстрая сортировка для массива int
    // ------------------------------------------------------------
    private void quickSort3Way(int[] a, int lo, int hi) {
        // Аналогичная реализация для целых чисел (концы отрезков)
        while (lo < hi) {
            int lt = lo, gt = hi, i = lo;
            int pivot = a[lo + (hi - lo) / 2];
            while (i <= gt) {
                if (a[i] < pivot) {
                    swap(a, i, lt);
                    i++;
                    lt++;
                } else if (a[i] > pivot) {
                    swap(a, i, gt);
                    gt--;
                } else {
                    i++;
                }
            }
            if (lt - 1 - lo < hi - (gt + 1)) {
                quickSort3Way(a, lo, lt - 1);
                lo = gt + 1;
            } else {
                quickSort3Way(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
    }

    // Обмен двух целых чисел
    private void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // ------------------------------------------------------------
    // Бинарные поиски
    // ------------------------------------------------------------
    // Возвращает количество отрезков, у которых start <= p (upper bound)
    // Используется бинарный поиск в массиве отрезков, отсортированном по start.
    private int upperBoundByStart(Segment[] seg, int p) {
        int lo = 0, hi = seg.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (seg[mid].start <= p) {   // текущий отрезок подходит, ищем дальше вправо
                lo = mid + 1;
            } else {                     // слишком большой start, ищем влево
                hi = mid;
            }
        }
        return lo; // количество элементов <= p
    }

    // Возвращает количество элементов массива a, строго меньших x (lower bound)
    private int lowerBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (a[mid] < x) {            // текущий элемент меньше x, ищем дальше
                lo = mid + 1;
            } else {                     // достигли первого элемента >= x
                hi = mid;
            }
        }
        return lo; // количество элементов < x
    }

    // ------------------------------------------------------------
    // Класс отрезок с корректным компаратором
    // ------------------------------------------------------------
    private class Segment implements Comparable<Segment> {
        int start;   // начало отрезка
        int stop;    // конец отрезка

        Segment(int start, int stop) {
            // Гарантируем, что start <= stop (корректный отрезок)
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
            // Сравниваем отрезки по началу (для сортировки)
            return Integer.compare(this.start, o.start);
        }
    }
}