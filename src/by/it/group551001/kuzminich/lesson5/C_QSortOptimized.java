package by.it.group551001.kuzminich.lesson5;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
    private int find_first_greater(int[] arr, int x){
        int left = 0;
        int right = arr.length -1;
        int res = arr.length;

        while (left<=right){
            int mid = left + (right - left) / 2;
            if (arr[mid] > x){
                res = mid;
                right = mid-1;
            }else{
                left = mid+1;
            }
        }
        return res;
    }
    private void swap(Segment[] arr, int i, int j){
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void quick_sort_3_steps(Segment[] arr, int left, int right){
        //base case: (otrezok empty or contains 1 elem)
        if (left>=right) return;
        //oporny element
        while (left<right) {
            Segment opora = arr[left];
            int small = left; // элементы до arr[small] меньше опорного
            int i = left + 1; //текущий элемент для проверки
            int big = right; // элементы после индекса big больше опорного


            while (i <= big) {
                int cmp = arr[i].compareTo(opora);

                if (cmp < 0) { // текущий элемент меньше опорного, отправляем опорный в левую часть
                    swap(arr, small, i);
                    small++;
                    i++;
                } else if (cmp > 0) { //в правую часть отправка
                    swap(arr, i, big);
                    big--;
                } else { // текущий элемент равен опорному
                    i++;
                }

                // Элиминация хвостовой рекурсии:


            }
            if (small - left < right - big) {
                quick_sort_3_steps(arr, left, small - 1);
                left = big + 1;
            } else {
                quick_sort_3_steps(arr, big + 1, right);
                right = small - 1;
            }
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
        quick_sort_3_steps(segments, 0, n-1);

        int[] starts = new int[n];
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            stops[i] = segments[i].stop;
        }
        Arrays.sort(stops);

        for (int i = 0; i<m; i++){
            int after_start = find_first_greater(starts, points[i]);
            int after_stop = find_first_greater(stops, points[i]-1);
            result[i] = after_start-after_stop;
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);;
        }

        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            if (this.start != o.start) return this.start-o.start;
            else return this.stop-o.stop;
        }
    }

}
