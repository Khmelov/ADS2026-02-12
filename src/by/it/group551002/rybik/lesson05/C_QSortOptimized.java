package by.it.group551002.rybik.lesson05;

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

        sort(segments, 0 ,n-1);

        for (int i = 0; i < m; i++ ){
            int a = binaryFind(segments, points[i]);
            while (a >= 0){
                if (segments[a].stop >=  points[i]) result[i]++;
                a--;
            }
        }











        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
            //подумайте, что должен возвращать компаратор отрезков
            return Integer.compare(this.start, o.start);
        }
    }

    int[] partition(Segment[] arr, int low, int high) {
        Segment pivot = arr[high];

        int lt = low;
        int i = low;
        int gt = high;

        while (i <= gt) {
            if (arr[i].compareTo(pivot) < 0) {
                Segment temp = arr[lt];
                arr[lt] = arr[i];
                arr[i] = temp;

                lt++;
                i++;
            } else if (arr[i].compareTo(pivot) > 0) {
                Segment temp = arr[i];
                arr[i] = arr[gt];
                arr[gt] = temp;

                gt--;
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    void sort(Segment[] arr, int low, int high) {
        while (low < high) {
            int[] mid = partition(arr, low, high);

            sort(arr, low, mid[0] - 1);

            low = mid[1] + 1;
        }
    }

    int binaryFind(Segment[] arr, int value){
        int left  = 0;
        int right = arr.length-1;


        while (left <= right ){
            int mid = (left+right) /2;
            if (arr[mid].start == value ){
                return mid;
            } else if (arr[mid].start > value){
                right = mid-1;
            } else {
                left = mid+1;
            }
        }

        return right;
    }




}
