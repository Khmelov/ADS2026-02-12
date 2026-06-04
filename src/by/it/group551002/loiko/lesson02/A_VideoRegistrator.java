package by.it.group551002.loiko.lesson02;

import java.util.ArrayList;
import java.util.List;
/*
Даны события events
реализуйте метод calcStartTimes, так, чтобы число включений регистратора на
заданный период времени (1) было минимальным, а все события events
были зарегистрированы.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1); //рассчитаем моменты старта, с длиной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        //events - события которые нужно зарегистрировать
        //timeWorkDuration время работы видеокамеры после старта
        List<Double> result = new ArrayList<>();

        // ШАГ 1: Сортируем события по возрастанию
        // Это важно для жадного алгоритма!
        double[] sortedEvents = events.clone();  // создаем копию, чтобы не испортить оригинал
        java.util.Arrays.sort(sortedEvents);

        // ШАГ 2: Жадный алгоритм
        int i = 0;  // индекс текущего события
        int n = sortedEvents.length;

        while (i < n) {
            // Берем самое левое незарегистрированное событие
            double currentEvent = sortedEvents[i];

            // Ставим видеокамеру так, чтобы она покрыла это событие
            // Лучший момент старта = currentEvent (начало события)
            double startTime = currentEvent;
            result.add(startTime);

            // Вычисляем момент окончания работы камеры
            double endTime = startTime + workDuration;

            // Пропускаем все события, которые попадают в интервал [startTime, endTime]
            while (i < n && sortedEvents[i] <= endTime) {
                i++;  // это событие уже зарегистрировано
            }
        }

        return result;
    }
}
