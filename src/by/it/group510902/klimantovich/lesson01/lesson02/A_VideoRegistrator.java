package by.it.group510902.klimantovich.lesson01.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<Double> starts = instance.calcStartTimes(events, 1); //рассчитаем моменты старта, с длинной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        //events - события которые нужно зарегистрировать
        //timeWorkDuration время работы видеокамеры после старта
        List<Double> result;
        result = new ArrayList<>();

        // Шаг 1: Сортируем события по времени
        Arrays.sort(events);

        int i = 0;                              //i - это индекс события events[i]
        int n = events.length;

        // Шаг 2: Жадный алгоритм
        while (i < n) {
            // Текущее событие - начало работы камеры
            double startTime = events[i];
            result.add(startTime);

            // Вычисляем время окончания работы камеры
            double endTime = startTime + workDuration;

            // Пропускаем все события, которые попадают в интервал работы камеры
            i++; // Переходим к следующему событию
            while (i < n && events[i] <= endTime) {
                i++;
            }
            // Теперь i указывает на первое событие, которое не покрыто текущим включением
        }

        return result;                        //вернем итог
    }
}