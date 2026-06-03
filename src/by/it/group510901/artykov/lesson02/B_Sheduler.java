package by.it.group510901.artykov.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/*
 * Даны интервальные события.
 *
 * Необходимо выбрать максимальное количество
 * непересекающихся событий.
 *
 * Используется жадный алгоритм:
 * всегда выбирается событие,
 * которое заканчивается раньше остальных.
 */

public class B_Sheduler {

    public static void main(String[] args) {

        B_Sheduler instance = new B_Sheduler();

        Event[] events = {
                new Event(0, 3),
                new Event(0, 1),
                new Event(1, 2),
                new Event(3, 5),
                new Event(1, 3),
                new Event(1, 3),
                new Event(1, 3),
                new Event(3, 6),
                new Event(2, 7),
                new Event(2, 3),
                new Event(2, 7),
                new Event(7, 9),
                new Event(3, 5),
                new Event(2, 4),
                new Event(2, 3),
                new Event(3, 7),
                new Event(4, 5),
                new Event(6, 7),
                new Event(6, 9),
                new Event(7, 9),
                new Event(8, 9),
                new Event(4, 6),
                new Event(8, 10),
                new Event(7, 10)
        };

        // Рассчитываем оптимальное расписание
        List<Event> starts = instance.calcStartTimes(events, 0, 10);

        // Вывод результата
        System.out.println(starts);
    }

    /*
     * Метод выбирает максимальное количество
     * непересекающихся событий.
     *
     * events - массив событий
     * from - начало периода
     * to - конец периода
     *
     * Алгоритм:
     * 1. Сортируем события по времени окончания
     * 2. Берём событие с самым ранним окончанием
     * 3. Следующее событие должно начинаться
     *    не раньше окончания предыдущего
     */
    List<Event> calcStartTimes(Event[] events, int from, int to) {

        // Список выбранных событий
        List<Event> result = new ArrayList<>();

        /*
         * Сортировка:
         * сначала по времени окончания,
         * затем по времени начала
         */
        Arrays.sort(events, new Comparator<Event>() {

            @Override
            public int compare(Event o1, Event o2) {

                // Сравнение по окончанию
                if (o1.stop != o2.stop) {
                    return o1.stop - o2.stop;
                }

                // Если окончания равны —
                // сравниваем по началу
                return o1.start - o2.start;
            }
        });

        // Время окончания последнего выбранного события
        int currentTime = from;

        // Проходим по всем событиям
        for (Event event : events) {

            /*
             * Проверяем:
             * 1. Событие начинается после currentTime
             * 2. Событие находится в нужном диапазоне
             */
            if (event.start >= currentTime && event.stop <= to) {

                // Добавляем событие
                result.add(event);

                // Обновляем текущее время
                currentTime = event.stop;
            }
        }

        // Возвращаем результат
        return result;
    }

    /*
     * Класс события.
     *
     * start - начало
     * stop - конец
     */
    static class Event {

        int start;
        int stop;

        Event(int start, int stop) {

            this.start = start;
            this.stop = stop;
        }

        /*
         * Красивый вывод события.
         */
        @Override
        public String toString() {

            return "(" + start + ":" + stop + ")";
        }
    }
}