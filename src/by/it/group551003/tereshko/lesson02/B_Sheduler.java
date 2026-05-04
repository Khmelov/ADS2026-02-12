package by.it.group551003.tereshko.lesson02;

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

    void sortEventsEnds(Event[] events, int low, int high) {
        if (low < high) {
            int pivotIndex;
            pivotIndex = partition(events, low, high);
            sortEventsEnds(events, low, pivotIndex - 1);
            sortEventsEnds(events, pivotIndex + 1, high);
        }
    }

    int partition(Event[] events, int low, int high) {
        int pivot, left;
        Event temp;

        pivot = events[high].stop;
        left = low - 1;

        for (int i = low; i < high; i++) {
            if (events[i].stop <= pivot) {
                left++;
                temp = events[left];
                events[left] = events[i];
                events[i] = temp;
            }
        }
        temp = events[left + 1];
        events[left + 1] = events[high];
        events[high] = temp;
        return left + 1;
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        //Events - события которые нужно распределить в аудитории
        //в период [from, to] (включительно).
        //оптимизация проводится по наибольшему числу непересекающихся событий.
        //Начало и конец событий могут совпадать. нада =.
        List<Event> result;
        result = new ArrayList<>();
        //ваше решение.
        sortEventsEnds(events, 0, events.length - 1);
        result.add(events[0]);
        for (Event event : events) {
            if (result.get(result.size() - 1).stop <= event.start) {
                result.add(event);
            }
        }
        return result;          //вернем итог
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