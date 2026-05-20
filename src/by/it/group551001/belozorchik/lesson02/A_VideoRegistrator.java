package by.it.group551001.belozorchik.lesson02;

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
        int i = 0;                              //i - это индекс события events[i]

        //Подготовка к жадному поглощению массива событий
        //hint: сортировка Arrays.sort обеспечит скорость алгоритма
        //C*(n log n) + C1*n = O(n log n)
        Arrays.sort(events);
        int n = events.length;

        while (i < n) {  //пока есть незарег события
            //берём самое левое
            double start = events[i];
            //становится стартом видеокамеры
            result.add(start);
            //вычисляем момент окончания работы видеокамеры
            double end = (start + 1); // events[i] + 1
            i++;
            //пропуск всех событий, кот попадут в интервал [start, end]
            while ((i < n) && (events[i] <= end)) {
                i++; // увелич индекс пока события поктыты текущ
            }
        }
        return result;                        //вернем итог
    }
}
