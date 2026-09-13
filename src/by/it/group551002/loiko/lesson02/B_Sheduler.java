    package by.it.group551002.loiko.lesson02;

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
                // ШАГ 1: Отбираем только события, которые попадают в наш временной интервал [from, to]
                List<Event> validEvents = new ArrayList<>();
                for (Event e : events) {
                    if (e.start >= from && e.stop <= to) {
                        validEvents.add(e);
                    }
                }

                // ШАГ 2: Сортируем события по времени ОКОНЧАНИЯ (по возрастанию)
                // Это ключевой момент жадного алгоритма!
                validEvents.sort((e1, e2) -> Integer.compare(e1.stop, e2.stop));

                // ШАГ 3: Жадный выбор событий
                int lastEndTime = from;  // время окончания последнего выбранного события

                for (Event event : validEvents) {
                    // Если событие начинается НЕ РАНЬШЕ, чем закончилось предыдущее
                    // (имеем право на совпадение: одно кончилось в 5, другое началось в 5)
                    if (event.start >= lastEndTime) {
                        result.add(event);           // берём это событие
                        lastEndTime = event.stop;    // обновляем время окончания
                    }
                }

                return result;
            }//вернем итог


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