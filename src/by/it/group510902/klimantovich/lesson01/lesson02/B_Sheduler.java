package by.it.group510902.klimantovich.lesson01.lesson02;

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

public class B_Sheduler {//основной класс программы
    public static void main(String[] args) {// метод содержащий занятия
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
        List<Event> validEvents = new ArrayList<>();// создает вспомогательный список, сюда будут добавляться события которые встанут в расписание
        for (Event event :events){// перебирает все элементы события из исходного массива
            if(event.start >= from && event.stop <=to){//проверка правильности события
                validEvents.add(event);// добавляет событие
            }
        }
        validEvents.sort(Comparator.comparingInt((e->e.stop)));//сортировка событий
        int lastEndTime = from;//создание переменной
        for (Event event : validEvents){// сортировк из уже отсортированного списка
            if (event.start >= lastEndTime){// проверка, чтобы время не накладывалось
                result.add(event);// добавляем событие в окончательный список
                lastEndTime=event.stop;// обновление окончания события, чтобы не начиналось раньше этого
            }
        }


        return result;          //вернем итог
    }

    //событие у аудитории(два поля: начало и конец)
    static class Event {// вложенный класс
        int start;// хранит время начала
        int stop;// хранит окончание события

        Event(int start, int stop) {// конструктор вызывается при создании нового объекта события
            this.start = start;// присваиваем полю текущего объекта значение переданное в конструктор
            this.stop = stop;// то же, что и в предыдущем
        }

        @Override// переопределение метода родительского класса
        public String toString() {
            return "(" + start + ":" + stop + ")";
        }// переопределяет чтобы выводилось красиво
    }
}