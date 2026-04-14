package by.it.group510901.zuev.lesson02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class B_Sheduler {
    public static void main(String[] args) {
        B_Sheduler instance = new B_Sheduler();
        Event[] events = {
                new Event(0, 3), new Event(0, 1), new Event(1, 2), new Event(3, 5),
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
        List<Event> result = new ArrayList<>();

        // 1. Отбираем события, полностью лежащие внутри интервала [from, to]
        List<Event> suitable = new ArrayList<>();
        for (Event e : events) {
            if (e.start >= from && e.stop <= to) {
                suitable.add(e);
            }
        }

        // 2. Сортируем подходящие события по времени окончания (stop)
        Collections.sort(suitable, Comparator.comparingInt(e -> e.stop));

        // 3. Жадный выбор: первое событие всегда берём, дальше только те,
        //    которые начинаются не раньше окончания предыдущего выбранного
        int lastStop = from;  // начальное значение – левая граница интервала
        for (Event e : suitable) {
            if (e.start >= lastStop) {
                result.add(e);
                lastStop = e.stop;
            }
        }

        return result;
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