package by.it.group551004.kondratova.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class A_VideoRegistrator {

    public List<Double> calcStartTimes(double[] events, double workDuration) {
        List<Double> starts = new ArrayList<>();

        // Сортируем события
        double[] sorted = events.clone();
        Arrays.sort(sorted);

        int i = 0;
        while (i < sorted.length) {
            double start = sorted[i];
            starts.add(start);
            double end = start + workDuration;
            // Пропускаем все события, которые покрываются текущим стартом
            while (i < sorted.length && sorted[i] <= end) {
                i++;
            }
        }

        return starts;
    }
}