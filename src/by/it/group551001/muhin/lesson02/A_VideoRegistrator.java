package by.it.group551001.muhin.lesson02;

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
        List<Double> starts = instance.calcStartTimes(events, 1);
        System.out.println(starts);
    }


    List<Double> calcStartTimes(double[] events, double workDuration) {
        List<Double> result;
        result = new ArrayList<>();
        int i = 0; //i - это индекс события events[i]
        for(int z = 0; z < events.length; z++) {
            for(int j = 1; j < events.length; j++) {
                if (events[j - 1] > events[j]) {
                    double temp = events[j - 1];
                    events[j - 1] = events[j];
                    events[j] = temp;
                }
            }
        }
        double end_of_record = events[i] + workDuration;
        result.add(events[i]);
        while(i < events.length) {
            while (i < events.length && events[i] <= end_of_record) {
                i++;
            }
            if (i < events.length) {
                end_of_record = events[i] + workDuration;
                result.add(events[i]);
            }
        }

        return result;
    }
}
