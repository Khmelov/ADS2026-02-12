package by.it.group551004.fedoruk.lesson05;

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

    public int Partition(Segment[] arr, int left, int right){
        Segment pivot = arr[(left + right) / 2];
        while (left <= right){
            while (arr[left].compareTo(pivot) == -1){
                left++;
            }
            while (arr[right].compareTo(pivot) == 1){
                right--;
            }
            if (left <= right){
                Segment temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
                left++;
                right--;
            }
        }
        return left;
    }

    private int findLastSegment(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int answer = -1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (segments[mid].start <= point) {
                answer = mid;
                left = mid + 1; // идём вправо
            } else {
                right = mid - 1;
            }
        }
        return answer;
    }

    public void QuickSort(Segment[] arr, int left, int right){
        // с учетом элиминации хвостовой рекурсии
        while (left < right){
            int pivot = Partition(arr,left,right);
            QuickSort(arr, left, pivot - 1);
            left = pivot;
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
        QuickSort(segments,0, segments.length - 1);

        for (int i = 0; i < m; i++) {
            int point = points[i];

            int index = findLastSegment(segments, point);

            for (int j = index; j >= 0; j--) {
                if (segments[j].start > point)
                    continue;

                if (segments[j].stop >= point)
                    result[i]++;
            }
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment  implements Comparable<Segment>{
        int start;
        int stop;

        Segment(int start, int stop){
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Segment o) {
            if (this.start > o.start) {
                return 1;
            }
            else if (this.start == o.start){
                if (this.stop > o.stop) {
                    return 1;
                }
                else if (this.stop == o.stop){
                    return 0;
                }
                return -1;
            }
            return -1;
        }
    }


}
