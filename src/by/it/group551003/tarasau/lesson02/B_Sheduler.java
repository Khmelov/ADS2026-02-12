package by.it.group551003.tarasau.lesson02;

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
        Event[] events = {
                new Event(0, 3), new Event(0, 1), new Event(1, 2), new Event(3, 5),
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

        ArrayList<Event> sortedList = new ArrayList<>(Arrays.asList(events));

        quickSortAttempt(sortedList, 0, sortedList.size()-1);


        int fixed = 0;
        while (fixed < to+1) {
            for (int i = 0; i < sortedList.size(); i++) {
                if (sortedList.get(i).start == fixed) {
                    result.add(sortedList.get(i));
                    fixed = sortedList.get(i).stop;
                    i = 0;
                }
            }
            ++fixed;
        }






        return result;          //вернем итог
    }

    void quickSortAttempt(ArrayList<Event> list, int begin, int end) {
        if (begin < end) {
            int p = partition(list, begin, end);
            quickSortAttempt(list, begin, p - 1);
            quickSortAttempt(list, p + 1, end);
        }
    }

    int partition(ArrayList<Event> list, int begin, int end) {
        int i = begin;
        int j = end+1;
        Event pivot = list.get(begin);

        while (true) {
            while (list.get(++i).difference < pivot.difference) {
                if (i == end) break;
            }

            while (list.get(--j).difference > pivot.difference) {
                if (j == begin) break;
            }

            if (i >= j) break;

            swap(list, i, j);
        }

        swap(list, begin, j);

        return j;
    }

    void swap(ArrayList<Event> list, int i, int j) {
        Event temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    //событие у аудитории(два поля: начало и конец)
    static class Event {
        int start;
        int stop;
        int difference;

        Event(int start, int stop) {
            this.start = start;
            this.stop = stop;
            this.difference = stop - start;
        }

        @Override
        public String toString() {
            return "(" + start + ":" + stop + ")";
        }
    }
}