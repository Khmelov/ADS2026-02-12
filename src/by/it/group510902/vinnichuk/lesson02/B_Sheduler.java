package by.it.group510902.vinnichuk.lesson02;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
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
        List<Event> result= new ArrayList<>();

        //ваше решение.
// Сначала я сортирую все события по времени их окончания.
        // Это и есть мой жадный шаг: чем раньше закончится текущее занятие,
        // тем больше места останется в аудитории для следующих.
        Arrays.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return Integer.compare(e1.stop, e2.stop);
            }
        });

        // Завожу переменную для отслеживания времени, когда аудитория освободится.
        // Изначально это время 'from'.
        int currentTime = from;

        // Теперь прохожу по всем отсортированным событиям.
        for (Event event : events) {
            // Если начало события попадает в мой заданный период [from, to]
            // и если оно начинается не раньше, чем освободится аудитория:
            if (event.start >= currentTime && event.stop <= to) {
                // Значит, я могу принять это событие.
                result.add(event);
                // И теперь аудитория будет занята до конца этого события.
                currentTime = event.stop;
            }
        }

        return result; // Возвращаю список максимально возможного количества событий
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