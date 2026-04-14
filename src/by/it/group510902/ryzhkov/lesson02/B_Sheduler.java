package by.it.group510902.ryzhkov.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
/*
Даны интервальные события events
реализуйте метод calcStartTimes, так, чтобы число принятых к выполнению
непересекающихся событий было максимально.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

//import javafx.util.Pair;

public class B_Sheduler {
    public static void main(String[] args) {
        B_Sheduler instance = new B_Sheduler();
        Event[] events = { new Event(0, 3), new Event(0, 1), new Event(1, 2), new Event(3, 5),
                new Event(1, 3), new Event(1, 3), new Event(1, 3), new Event(3, 6),
                new Event(2, 7), new Event(2, 3), new Event(2, 7), new Event(7, 9),
                new Event(3, 5), new Event(2, 4), new Event(2, 3), new Event(3, 7),
                new Event(4, 5), new Event(6, 7), new Event(6, 9), new Event(7, 9),
                new Event(8, 9), new Event(4, 6), new Event(8, 10), new Event(7, 10)
        };

        List<Event> starts = instance.calcStartTimes(events, 0, 10); // рассчитаем оптимальное заполнение аудитории
        System.out.println(starts); // покажем рассчитанный график занятий
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        // Events - события которые нужно распределить в аудитории
        // в период [from, int] (включительно).
        // оптимизация проводится по наибольшему числу непересекающихся событий.
        // Начало и конец событий могут совпадать.
        List<Event> result;
        result = new ArrayList<>();
        // ваше решение.

        Arrays.sort(events, new Comparator<Event>() {
            public int compare(Event o1, Event o2) {
                return o1.stop - o2.stop;
            };
        });

        List<Event> events_list = new ArrayList<>();
        for (Event event : events) {
            if (event.start >= from && event.stop <= to) {
                events_list.add(event);
            }
        }

        while (events_list.size() != 0) {
            if (events_list.size() == 1) {
                result.add(events_list.get(0));
                break;
            }
            for (int i = 1; i < events_list.size(); i++) {
                if (events_list.get(i).start < events_list.get(0).stop) {
                    events_list.remove(i);
                    i--;
                }
            }

            result.add(events_list.get(0));
            events_list.remove(0);
        }

        return result; // вернем итог
    }

    // событие у аудитории(два поля: начало и конец)
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