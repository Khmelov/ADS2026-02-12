package by.it.group551002.efimchikov.lesson05;

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

    public static int[] partion3(Segment[] segments,int L,int R){
        int C=(R+L)/2;
        if(segments[C].compareTo(segments[L])==1){
            Segment temp=segments[C];
            segments[C]=segments[L];
            segments[L]=temp;
        }
        if(segments[R].compareTo(segments[L])==1){
            Segment temp=segments[L];
            segments[L]=segments[R];
            segments[R]=temp;
        }
        if(segments[C].compareTo(segments[R])==1){
            Segment temp=segments[C];
            segments[C]=segments[R];
            segments[R]=temp;
        }

        Segment x=segments[R];

        int i=L,j=R,Lt=i;

        while(Lt<=j){
            int cmp=segments[Lt].start-x.start;
            if(cmp<0){
                Segment temp=segments[Lt];
                segments[Lt]=segments[i];
                segments[i]=temp;
                Lt++;i++;
            }else if(cmp>0){
                Segment temp=segments[Lt];
                segments[Lt]=segments[j];
                segments[j]=temp;
                j--;
            }else{
                Lt++;
            }
        }
        return new int[]{i,j};
    }

    public static void Qsort(Segment[] segments,int L,int R){
        while(L<R){
            int[] LtRt=partion3(segments,L,R);
            int i=LtRt[0];
            int j=LtRt[1];
            int LftLen=i-L;
            int RLen=R-j;
            if(LftLen<RLen){
                Qsort(segments,L,i-1);
                L=j+1;
            }else{
                Qsort(segments,j+1,R);
                R=i-1;
            }
        }
    }

    public static int upperbound(Segment[] segments,int point){
        int L=0,R=segments.length;
        while(L<R){
            int C=(L+R)/2;
            if(segments[C].start<=point){
                L=C+1;
            }else{
                R=C;
            }
        }
        return L;
    }
    public static int lowerbound(int[] stops,int point){
        int L=0,R=stops.length;
        while(L<R){
            int C=(L+R)/2;
            if(stops[C]<point){
                L=C+1;
            }else{
                R=C;
            }
        }
        return L;
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
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
            stops[i]=segments[i].stop;
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением быстрой сортировки
        //в классе отрезка Segment реализуйте нужный для этой задачи компаратор

        Qsort(segments,0,n-1);
        Arrays.sort(stops);
        for(int i=0;i<points.length;i++){
            int R=upperbound(segments,points[i]);
            int L=lowerbound(stops,points[i]);
            result[i]=L-R;
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
            if(this.start<this.stop){
                int temp=this.start;
                this.start=this.stop;
                this.stop=temp;
            }
        }

        @Override
        public int compareTo(Object o) {
            Segment oth=(Segment) o;
            if(this.start<oth.start){
                return 1;
            }else{
                if(this.start!=oth.start) return -1;
            }
            if(this.stop<oth.stop){
                return 1;
            }else{
                if(this.stop!=oth.stop)return -1;
            }
            return 0;
        }
    }

}
