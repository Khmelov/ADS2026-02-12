package by.it.group510901.filimonova.lesson01.lesson02;

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
        //в период [from, to] (включительно).
        //оптимизация проводится по наибольшему числу непересекающихся событий.
        //Начало и конец событий могут совпадать.
        List<Event> result = new ArrayList<>();

        //Фильтруем события, которые полностью находятся в интервале [from, to]
        // и отбрасываем события с началом после to или концом до from
        List<Event> validEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.start >= from && event.stop <= to) {
                validEvents.add(event);
            }
        }

        //Сортируем события по времени окончания (жадный выбор)
        // Если времена окончания совпадают, сортируем по времени начала
        validEvents.sort(new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                if (e1.stop != e2.stop) {
                    return Integer.compare(e1.stop, e2.stop);
                }
                return Integer.compare(e1.start, e2.start);
            }
        });

        //Жадный алгоритм - выбираем события, которые не пересекаются
        int lastEndTime = from; // Время окончания последнего выбранного события

        for (Event event : validEvents) {
            // Если событие начинается не раньше, чем закончилось последнее выбранное
            if (event.start >= lastEndTime) {
                result.add(event);
                lastEndTime = event.stop;
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
/*1. Фильтруем события, оставляя только те, что полностью внутри [from, to]
2. Сортируем события по времени окончания (по возрастанию)
3. Выбираем первое событие (самое раннее окончание)
4. Выбираем следующее событие, которое начинается не раньше, чем закончилось предыдущее
5. Повторяем шаг 4
*/