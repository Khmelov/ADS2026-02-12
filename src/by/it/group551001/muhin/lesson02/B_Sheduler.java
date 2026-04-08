package by.it.group551001.muhin.lesson02;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
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

        List<Event> starts = instance.calcStartTimes(events, 0, 10);
        System.out.println(starts);
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        List<Event> result;
        result = new ArrayList<>();
        int i = 0;
        QuickSort(events,0, events.length - 1);
        int LastStop = from;
        for (i = 0; i < events.length; i++) {
            if (events[i].start >= LastStop && events[i].stop <= to) {
                result.add(events[i]);
                LastStop = events[i].stop;
            }
        }

        return result;
    }

    void QuickSort (Event[] events,int left, int right) {
        Event pivot = events[(left + right) / 2];
        int i = left;
        int j = right;
        Event temp;
        while (i <= j) {
            while (compare(events[i],pivot) < 0)
                i++;
            while (compare(events[j],pivot) > 0)
                j--;
            if (i <= j) {
                temp = events[i];
                events[i] = events[j];
                events[j] = temp;
                i++;
                j--;
            }
        }
        if (j > left)
            QuickSort(events,left,j);
        if (i < right)
            QuickSort(events,i,right);
    }

    int compare(Event a, Event b) {
        if (a.stop != b.stop) return a.stop - b.stop;
        return a.start - b.start;
    }


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