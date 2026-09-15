package by.it.group510901.filimonova.lesson01.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class  A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1); //рассчитаем моменты старта, с длинной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        //events - события которые нужно зарегистрировать
        //timeWorkDuration время работы видеокамеры после старта
        List<Double> result = new ArrayList<>();

        // Сортируем события по возрастанию
        double[] sortedEvents = events.clone();
        Arrays.sort(sortedEvents);

        int i = 0; // индекс текущего события

        // пока есть незарегистрированные события
        while (i < sortedEvents.length) {
            // берем текущее событие как левую границу
            double currentEvent = sortedEvents[i];

            // запоминаем время старта видеокамеры
            double startTime = currentEvent;

            // добавляем время старта в результат
            result.add(startTime);

            // вычисляем момент окончания работы видеокамеры
            double endTime = startTime + workDuration;

            // пропускаем все события, которые покрываются текущим сеансом
            // (события, время которых <= endTime)
            while (i < sortedEvents.length && sortedEvents[i] <= endTime) {
                i++;
            }
        }

        return result; // возвращаем итог
    }
}
/*Алгоритм (жадный):
1. Сортируем все события по времени
2. Берем самое левое незарегистрированное событие
3. Запускаем камеру в этот момент
4. Камера работает workDuration времени → покрывает все события до startTime + workDuration
5. Пропускаем все покрытые события
6. Повторяем шаги 2-5 для оставшихся*/