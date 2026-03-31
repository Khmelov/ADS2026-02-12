package by.it.group551003.kalach.lesson02;

import java.util.ArrayList;
import java.util.List;
/*
Даны события events
реализуйте метод calcStartTimes, так чтобы число включений регистратора на
заданный период времени (1) было минимальным, а все события events
были зарегистрированы.
*/

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1);
        System.out.println(starts);
    }
//
    List<Double> calcStartTimes(double[] events, double workDuration) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < events.length - 1; i++) {
            for (int j = 0; j < events.length - i - 1; j++) {
                if (events[j] > events[j + 1]) {
                    double temp = events[j];
                    events[j] = events[j + 1];
                    events[j + 1] = temp;
                }
            }
        }

        int i = 0;
        int n = events.length;

        while (i < n) {
            double startTime = events[i];
            result.add(startTime);
            double endTime = startTime + workDuration;

            while (i < n && events[i] <= endTime) {
                i++;
            }
        }

        return result;
    }
}