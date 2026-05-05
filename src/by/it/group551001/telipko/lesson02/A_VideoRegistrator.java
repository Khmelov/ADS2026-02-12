package by.it.group551001.telipko.lesson02;

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
        List<Double> starts = instance.calcStartTimes(events, 1);
        System.out.println(starts);
    }

    List<Double> calcStartTimes(double[] events, double workDuration) {
        //events - события которые нужно зарегистрировать
        //workDuration время работы видеокамеры после старта
        List<Double> result = new ArrayList<>();

        int index = 0;
        int lastIndex = events.length-1;
        double startTime;
        double endTime;

        Arrays.sort(events);

        while (index < lastIndex) {
            startTime = events[index];
            endTime = startTime + workDuration;
            result.add(startTime);
            while (events[index] <= endTime && index < lastIndex){
                index++;
            }
        }

        return result;
    }
}
