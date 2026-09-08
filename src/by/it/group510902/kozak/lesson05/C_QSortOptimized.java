package by.it.group510902.kozak.lesson05;

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
        quickSort(segments, 0, segments.length - 1);
        for (int i = 0; i < m; i++) {
            result[i] = countCovering(segments, points[i]);
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    private void quickSort(Segment[] a, int lo, int hi) {
        while (lo < hi) {

            // ── 3-разбиение (схема Bentley–McIlroy) ──────────────────────────
            Segment pivot = a[lo + (hi - lo) / 2]; // опорный — середина отрезка

            int lt = lo;   // граница зоны «< pivot»
            int gt = hi;   // граница зоны «> pivot»
            int i  = lo;   // текущий элемент

            while (i <= gt) {
                int cmp = a[i].compareTo(pivot);
                if      (cmp < 0) swap(a, lt++, i++); // a[i] < pivot
                else if (cmp > 0) swap(a, i, gt--);   // a[i] > pivot (i не двигаем!)
                else              i++;                  // a[i] == pivot
            }
            // Теперь: a[lo..lt-1] < pivot,  a[lt..gt] == pivot,  a[gt+1..hi] > pivot

            // ── Элиминация хвостовой рекурсии ─────────────────────────────────
            // Рекурсия только для МЕНЬШЕЙ части; бо́льшую часть — в следующую итерацию
            if (lt - lo < hi - gt) {
                quickSort(a, lo, lt - 1);   // рекурсия для левой (меньшей)
                lo = gt + 1;                // итерация для правой
            } else {
                quickSort(a, gt + 1, hi);   // рекурсия для правой (меньшей)
                hi = lt - 1;                // итерация для левой
            }
        }
    }
    private void swap(Segment[] a, int i, int j) {
        Segment tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    private int countCovering(Segment[] segments, int p) {
        // ── Бинарный поиск последнего отрезка с start <= p ───────────────────
        int lo = 0, hi = segments.length - 1, idx = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (segments[mid].start <= p) {
                idx = mid;       // mid подходит; запоминаем и ищем правее
                lo = mid + 1;
            } else {
                hi = mid - 1;   // start > p; ищем левее
            }
        }


        if (idx == -1) return 0; // все отрезки начинаются правее p
        int count = 0;
        for (int i = idx; i >= 0; i--) {
            if (segments[i].stop >= p) count++;
            // Примечание: нет смысла делать break — у отрезков левее stop может
            // быть любым (массив отсортирован по start, не по stop).
        }
        return count;
    }
    //отрезок
    private class Segment implements Comparable {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Object o) {
            Segment other = (Segment) o;
            if (this.start != other.start)
                return Integer.compare(this.start, other.start);
            return Integer.compare(this.stop, other.stop);
        }
    }

}
