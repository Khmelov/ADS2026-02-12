package by.it.group510901.sinyak.lesson05;

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

        quickSort(segments, 0, n - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];
            int count = 0;

            // Бинарный поиск: ищем самый правый индекс, где segments[index].start <= point
            int low = 0;
            int high = n - 1;
            int lastPossibleIdx = -1;

            while (low <= high) {

                int mid = low + (high - low) / 2;
                if (segments[mid].start <= point) {

                    lastPossibleIdx = mid;
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }

            // Теперь проверяем все отрезки от 0 до lastPossibleIdx
            if (lastPossibleIdx != -1) {
                for (int k = lastPossibleIdx; k >= 0; k--) {
                    if (segments[k].stop >= point) {
                        count++;
                    }
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
            return Integer.compare(this.start, other.start);
        }
    }

    public void quickSort(Segment[] arr, int left, int right) {
        while (left < right) {
            // Опорные индексы для 3-разбиения
            int lt = left; // Граница конца элементов < pivot
            int gt = right; // Граница начала элементов > pivot
            int i = left + 1;

            Segment pivot = arr[left];

            while (i <= gt) {
                int cmp = arr[i].compareTo(pivot);
                if (cmp < 0) {
                    swap(arr, lt++, i++);
                } else if (cmp > 0) {
                    swap(arr, i, gt--);
                } else {
                    i++;
                }
            }
            // Элиминация хвостовой рекурсии:
            // Сначала рекурсивно сортируем меньшую часть,
            // а большую оставляем для следующей итерации цикла
            if (lt - left < right - gt) {
                quickSort(arr, left, lt - 1);
                left = gt + 1;
            } else {
                quickSort(arr, gt + 1, right);
                right = lt - 1;
            }
        }
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
