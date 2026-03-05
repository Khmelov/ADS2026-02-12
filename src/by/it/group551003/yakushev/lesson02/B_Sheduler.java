package by.it.group551003.yakushev.lesson02;

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

        List<Event> starts = instance.calcStartTimes(events, 0, 10);  //рассчитаем оптимальное заполнение аудитории
        System.out.println(starts);                                 //покажем рассчитанный график занятий
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        List<Event> result;
        result = new ArrayList<>();
        Arrays.sort(events);

        int i = 0;
        while (i < events.length && (events[i].start < to)) {
            if (events[i].start < from) {
                ++i;
                continue;
            }
            if (result.isEmpty()) {
                result.add(events[i]);
                ++i;
                continue;
            }
            if (events[i].start < result.getLast().stop) {
                ++i;
                continue;
            }
            result.add(events[i]);
            ++i;
        }

        return result;          //вернем итог
    }

    static class Event implements Comparable<Event> {
        int start;
        int stop;

        Event(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public int compareTo(Event o) {
            return Integer.compare(stop, o.stop);
        }

        @Override
        public String toString() {
            return "(" + start + ":" + stop + ")";
        }
    }
}