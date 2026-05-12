package by.it.group551002.efimchikov.lesson02;

import java.util.ArrayList;
import java.util.List;
/*
Даны интервальные события events
реализуйте метод calcStartTimes, так, чтобы число принятых к выполнению
непересекающихся событий было максимально.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class B_Sheduler {
    public static void main(String[] args) {
        B_Sheduler instance = new B_Sheduler();
        Event[] events = {new Event(0, 3), new Event(0, 1), new Event(1, 2), new Event(3, 5),
                new Event(1, 3), new Event(1, 3), new Event(1, 3), new Event(3, 6),
                new Event(2, 7), new Event(2, 3), new Event(2, 7), new Event(7, 9),
                new Event(3, 5), new Event(2, 4), new Event(2, 3), new Event(3, 7),
                new Event(4, 5), new Event(6, 7), new Event(6, 9), new Event(7, 9),
                new Event(8, 9), new Event(4, 6), new Event(8, 10), new Event(7, 10)
        };

        List<Event> starts = instance.calcStartTimes(events, 0, 10);  //рассчитаем оптимальное заполнение аудитории
        System.out.println(starts);                                 //покажем рассчитанный график занятий
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        //Events - события которые нужно распределить в аудитории
        //в период [from, int] (включительно).
        //оптимизация проводится по наибольшему числу непересекающихся событий.
        //Начало и конец событий могут совпадать.
        List<Event> result;
        result = new ArrayList<>();
        //ваше решение.
        Qsort(0,events.length-1,events);
        int ptr=0,i=0;
        while(ptr<events.length){
            result.add(events[ptr]);
            i=ptr+1;
            while((i<events.length)&&(events[ptr].stop>events[i].start))
                i++;
            ptr=i;
        }

        return result;          //вернем итог
    }

    static void Qsort(int L, int R, Event[] events){
        int i=L,j=R;
        Event pivot=events[(R+L)/2];
        do{
            while(compare(events[i],pivot)<0)i++;
            while(compare(events[j],pivot)>0)j--;
            if(i<=j){
                Event temp=events[i];
                events[i]=events[j];
                events[j]=temp;
                i++;j--;
            }
        }while(i<=j);
        if(L<j)Qsort(L,j,events);
        if(i<R)Qsort(i,R,events);
    }

    static int compare(Event o1,Event o2){
        if((o1.stop>o2.stop)||((o1.stop==o2.stop)&&(o1.start>o2.start)))return 1;
        if((o1.stop<o2.stop)||((o1.stop==o2.stop)&&(o1.start<o2.start)))return -1;
        return 0;
    }

    //событие у аудитории(два поля: начало и конец)
    static class Event {
        int start;
        int stop;

        Event(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public String toString() {
            return "(" + start + ":" + stop + ")";
        }
    }
}