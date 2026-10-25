package by.it.group510902.konchatov.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.text.Segment;

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
            int start = scanner.nextInt();
            int stop = scanner.nextInt();
            if (start > stop) {
                int temp = start;
                start = stop;
                stop = temp;
            }
            segments[i] = new Segment(start, stop);
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
            
            // Бинарный поиск первого отрезка, который может содержать точку
            int firstIndex = find(segments, point);
            
            if (firstIndex != -1) {
                int count = 0;
                for (int j = firstIndex; j >= 0 && segments[j].start <= point; j--) {
                    if (segments[j].stop >= point) {
                        count++;
                    }
                }
                for (int j = firstIndex + 1; j < n && segments[j].start <= point; j++) {
                    if (segments[j].stop >= point) {
                        count++;
                    }
                }
                result[i] = count;
            } else {
                result[i] = 0;
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    public int find(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (segments[mid].start <= point) {
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }
     private void quickSort(Segment[] segments, int low, int high) {
        while (low < high) {
            int l = low;
            int r = high;
            int i = low + 1;
            Segment pivot = segments[low];
            
            while (i <= r) {
                int c= segments[i].compareTo(pivot);
                if (c < 0) {
                    swap(segments, l++, i++);
                } else if (c > 0) {
                    swap(segments, i, r--);
                } else {
                    i++;
                }
            }

            quickSort(segments, low, l- 1);
        
            low = r + 1;
        }
    }
    
    // Вспомогательный метод для обмена элементов
    private void swap(Segment[] segments, int i, int j) {
        Segment temp = segments[i];
        segments[i] = segments[j];
        segments[j] = temp;
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

            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
        
        @Override
        public String toString() {
            return "[" + start + ", " + stop + "]";
        }
    }

}
