package by.it.group551001.maxim_belyak.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
        List<Event> result = new ArrayList<>();

        // 1. Сортируем события по времени окончания (stop).
        // Это самый важный шаг жадного алгоритма: чем раньше закончится текущее событие,
        // тем больше места останется для следующих.
        Arrays.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                // Если время окончания разное — сортируем по нему
                if (e1.stop != e2.stop) {
                    return Integer.compare(e1.stop, e2.stop);
                }
                // Если окончание совпадает, можно отсортировать по началу
                return Integer.compare(e1.start, e2.start);
            }
        });

        // 2. Идем по списку и выбираем подходящие события
        int currentTime = from; // Время, когда аудитория освободится

        for (Event event : events) {
            // Если событие начинается не раньше, чем освободилась аудитория,
            // и оно заканчивается до конца нашего рабочего периода 'to'
            if (event.start >= currentTime && event.stop <= to) {
                result.add(event);         // Добавляем в расписание
                currentTime = event.stop;  // Теперь аудитория занята до конца этого события
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