package by.it.group551002.marochkov.lesson02;

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
        quickSort(events, 0, events.length - 1);

        Event current = events[0];
        result.add(current);

        for (int i = 1; i < events.length; i++) {
            if (current.stop <= events[i].start) {
                current = events[i];
                result.add(current);
            }
        }

        return result;          //вернем итог
    }
    int partition(Event[] arr, int low, int high) {
        int pivotStop = arr[high].stop;
        int pivotStart = arr[high].start;

        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].stop < pivotStop || (arr[j].stop == pivotStop && arr[j].start < pivotStart)) {
                i++;

                Event temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
            }
        }

        Event temp = arr[high];
        arr[high] = arr[i + 1];
        arr[i + 1] = temp;

        return i + 1;
    }

    void quickSort(Event[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);

            quickSort(arr, pi + 1, high);
            quickSort(arr, low, pi - 1);
        }
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