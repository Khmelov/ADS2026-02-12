package by.it.group551001.teryokhin.lesson05;

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

    public Event[] quick_sort_cur_arr;

    int[] getAccessory2(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //число отрезков отсортированного массива
        int n = scanner.nextInt();
        //Segment[] segments = new Segment[n];\
        //число точек
        int m = scanner.nextInt();
        Event[] events = new Event[n + n + m];
        //int[] points = new int[m];
        int[] result = new int[m];

        //читаем сами отрезки
        for (int i = 0; i < n; i++) {
            //читаем начало и конец каждого отрезка
            //segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
            events[i + i] = new Event(scanner.nextInt(), false, true);
            events[i + i + 1] = new Event(scanner.nextInt(), false, false);
        }
        //читаем точки
        for (int i = 0; i < m; i++) {
            //points[i] = scanner.nextInt();
            events[n + n + i] = new Event(scanner.nextInt(), true, false);
        }

        quick_sort_cur_arr = events;
        _SortQuick(0, events.length - 1);

        int next_request_event_index = 0;
        int cur_opened_segments = 0;
        for(int i = 0;i < events.length;i++)
        {
            if(!events[i].type) //segment
            {
                cur_opened_segments += events[i].start ? 1 : -1;
            }
            else //request
            {
                result[next_request_event_index++] = cur_opened_segments;
            }
        }


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    void _SortQuick(int l, int r)
    {
        while(l < r)
        {
            Event cur_mid = quick_sort_cur_arr[(l + r) / 2];
            int i = l, j = r;
            while(i <= j)
            {
                while(quick_sort_cur_arr[i].compareTo(cur_mid) < 0)i++;

                while(quick_sort_cur_arr[j].compareTo(cur_mid) > 0)j--;

                if(i <= j)
                {
                    Event temp = quick_sort_cur_arr[i];
                    quick_sort_cur_arr[i] = quick_sort_cur_arr[j];
                    quick_sort_cur_arr[j] = temp;
                    i++;
                    j--;
                }
            }
            if(j - l < r - i)
            {
                if(l < j)_SortQuick(l, j);
                l = i;
            }
            else
            {
                if(i < r)_SortQuick(i, r);
                r = j;
            }
        }
    }

    private class Event implements Comparable<Event>
    {
        int x;
        Boolean type; //false - segment, true - request
        Boolean start; //for segments, defines whether it's their start or end

        Event(int x, Boolean type, Boolean start)
        {
            this.x = x;
            this.type = type;
            this.start = start;
        }

        @Override
        public int compareTo(Event other)
        {
            //if(this.x < other.x || (this.x == other.x && (this.start || (this.type && !other.start))))return -1;
            //else return 1;

            if(this.x != other.x)return Integer.compare(this.x, other.x);

            if(this.start != other.start)return this.start ? -1 : 1;

            if(this.type != other.type)return this.type ? -1 : 1;

            return 0;
        }
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
