package by.it.group551004.kozhedub.lesson05;

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

    static void quickSort (Segment[] a, int l, int r){
        while (l<r){
            int m = (int) (Math.random()*(r-l+1)+l);

            int cur = l;
            int num = r;

            Segment temp = a[r];
            a[r] = a[m];
            a[m] = temp;

            for (int i =l; i<num; i++){
                if (a[i].start<a[r].start){
                    temp = a[i];
                    a[i] = a[cur];
                    a[cur++] = temp;
                } else if (a[i].start==a[r].start){ //3-partition
                    num--;
                    a[i] = a[num];
                    a[num] = a[r];
                }
            }

            for (int i = 0; i<=(r-num); i++){
                temp = a[r-i];
                a[r-i] = a[cur];
                a[cur++] = temp;
            }


            quickSort(a, l, cur-(r-num+2));
            //qSort(a, m+1, r);
            l = cur;
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
        quickSort(segments, 0, segments.length-1);

        int l, r, med;
        for(int k = 0; k < points.length; k++){
            l=0;
            r=segments.length;

            while (l<r){
                med=(l+r)/2;
                if (segments[med].start<points[k]){
                    l=med+1;
                }else if (segments[med].start>points[k]){
                    r=med-1;
                }else{
                    r=med;
                    l=med;
                }
            }
            if (r>=segments.length) {r=segments.length-1;}
            for (int i = 0; i<=r; i++){
                if (segments[i].stop>=points[k]){
                    result[k]++;
                }
            }
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
            return 0;
        }
    }

}
