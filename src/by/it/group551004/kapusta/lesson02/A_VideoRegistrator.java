package by.it.group551004.kapusta.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Даны события events.
Реализуйте метод calcStartTimes так, чтобы число включений регистратора
на заданный период времени (1) было минимальным, а все события events
были зарегистрированы.
Алгоритм жадный.
*/

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1);
        System.out.println(starts);
    }

    List<Double> calcStartTimes(double[] events, double workDuration) {
        List<Double> result = new ArrayList<>();

        // Сортируем события
        Arrays.sort(events);

        int i = 0;
        while (i < events.length) {
            // Берём самое раннее непокрытое событие
            double start = events[i];
            result.add(start);

            // Камера работает до:
            double end = start + workDuration;

            // Пропускаем все события, которые попадают в интервал работы камеры
            while (i < events.length && events[i] <= end) {
                i++;
            }
        }

        return result;
    }
}
