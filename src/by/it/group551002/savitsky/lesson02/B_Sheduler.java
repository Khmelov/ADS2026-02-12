package by.it.group551002.savitsky.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
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

    void swap(Event[] arr, int i, int j){
        Event temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    int partition(Event[] arr, int low, int high){

        int pivot = arr[high].stop;
        int i = low - 1;

        for (int j = low; j < high; j++){
            if (arr[j].stop <= pivot){
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i+1, high);

        return i+1;
    }

    void Quicksort(Event[] arr, int low, int high){
        if (low < high){

            int Pivotindex = partition(arr, low, high);

            Quicksort(arr, low, Pivotindex-1);
            Quicksort(arr,Pivotindex+1, high);
        }
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        //Events - события которые нужно распределить в аудитории
        //в период [from, int] (включительно).
        //оптимизация проводится по наибольшему числу непересекающихся событий.
        //Начало и конец событий могут совпадать.
        List<Event> result;
        result = new ArrayList<>();
        //ваше решение.

        Quicksort(events, 0, events.length - 1);

        int laststop = from;
        for (int i = 0; i < events.length; i++){
            if (events[i].start >= laststop) {
                result.add(events[i]);
                laststop = events[i].stop;
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