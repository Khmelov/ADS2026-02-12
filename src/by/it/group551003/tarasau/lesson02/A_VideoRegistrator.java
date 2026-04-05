package by.it.group551003.tarasau.lesson02;

import java.lang.reflect.Array;
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
        List<Double> starts = instance.calcStartTimes(events, 1); //рассчитаем моменты старта, с длинной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        //events - события которые нужно зарегистрировать
        //timeWorkDuration время работы видеокамеры после старта
        List<Double> result;
        ArrayList<Double> arr = new ArrayList<>();
        result = new ArrayList<>();
        //Комментарии от проверочного решения сохранены для подсказки, но вы можете их удалить.
        //Подготовка к жадному поглощению массива событий
        //hint: сортировка Arrays.sort обеспечит скорость алгоритма
        //C*(n log n) + C1*n = O(n log n)
        for (double elem : events) {  // for-each woohoo
            arr.add(elem);
        }

        boolean isCool = true;
        int i = 0;
        double fixedElem;
        quickSortAttempt(arr, 0, arr.size()-1);

        while (i != arr.size()) {
            fixedElem = arr.get(i);
            result.add(fixedElem);

            while (i != arr.size() && arr.get(i) - fixedElem <= 1) {
                ++i;
            }
        }


        //пока есть незарегистрированные события
        //получим одно событие по левому краю
        //и запомним время старта видеокамеры
        //вычислим момент окончания работы видеокамеры
        //и теперь пропустим все покрываемые события
        //за время до конца работы, увеличивая индекс

        return result;                        //вернем итог
    }

    void quickSortAttempt(ArrayList<Double> arr, int begin, int end) {
        if (begin < end) {
            int p = partition(arr, begin, end);
            quickSortAttempt(arr, begin, p-1);
            quickSortAttempt(arr,p+1, end);
        }
    }

    int partition(ArrayList<Double> arr, int begin, int end) {
        int i = begin;
        int j = end + 1;
        double pivot = arr.get(begin);

        while (true) {
            while (arr.get(++i) < pivot) {
                if (i == end) break;
            }

            while (arr.get(--j) > pivot) {
                if (j == begin) break;
            }

            if (i >= j) break;

            swap(arr, i, j);
        }

        swap(arr, begin, j);

        return j;
    }

    void swap(ArrayList<Double> arr, int i, int j) {
        Double temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }
}
