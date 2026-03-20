package by.it.group551001.chaley.lesson2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        int i = 0;                              //i - это индекс события events[i]
        Arrays.sort(events);
        double startTime = events[0];
        result.add(startTime);
        i++;
        while(i < events.length){
            if(events[i] > startTime + workDuration){
                startTime = events[i];
                result.add(startTime);
            }
            i++;
        }
        return result;
    }
}
