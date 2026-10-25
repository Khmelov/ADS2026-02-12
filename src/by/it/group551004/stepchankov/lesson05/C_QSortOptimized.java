package by.it.group551004.stepchankov.lesson05;

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
        int[] stack = new int[n * 2];
        int top = -1;
        stack[++top] = 0;
        stack[++top] = n - 1;

        while (top >= 0) {
            int hi = stack[top--];
            int lo = stack[top--];

            if (lo >= hi) continue;

            int lt = lo, gt = hi;
            Segment pivot = segments[lo];
            int i = lo + 1;

            while (i <= gt) {
                int cmp = segments[i].compareTo(pivot);
                if (cmp < 0) {
                    Segment tmp = segments[lt];
                    segments[lt] = segments[i];
                    segments[i] = tmp;
                    lt++;
                    i++;
                } else if (cmp > 0) {
                    Segment tmp = segments[i];
                    segments[i] = segments[gt];
                    segments[gt] = tmp;
                    gt--;
                } else {
                    i++;
                }
            }

            if (lt - lo < hi - gt) {
                stack[++top] = gt + 1;
                stack[++top] = hi;
                stack[++top] = lo;
                stack[++top] = lt - 1;
            } else {
                stack[++top] = lo;
                stack[++top] = lt - 1;
                stack[++top] = gt + 1;
                stack[++top] = hi;
            }
        }

        for (int i = 0; i < m; i++) {
            int point = points[i];

            int left = 0, right = n;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (segments[mid].start <= point) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            int count = 0;
            for (int j = 0; j < left; j++) {
                if (segments[j].stop >= point) {
                    count++;
                }
            }

            result[i] = count;
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
            //подумайте, что должен возвращать компаратор отрезков
            Segment other = (Segment) o;
            if (this.start != other.start) {
                return this.start - other.start;
            }
            return this.stop - other.stop;
        }
    }

}
