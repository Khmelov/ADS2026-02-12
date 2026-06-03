package by.it.group510901.artykov.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Даны события events.
 *
 * Необходимо определить минимальное количество включений
 * видеорегистратора так, чтобы все события были записаны.
 *
 * Камера после включения работает:
 * workDuration времени.
 *
 * Используется жадный алгоритм.
 */

public class A_VideoRegistrator {

    public static void main(String[] args) {

        A_VideoRegistrator instance = new A_VideoRegistrator();

        double[] events = new double[]{
                1,
                1.1,
                1.6,
                2.2,
                2.4,
                2.7,
                3.9,
                8.1,
                9.1,
                5.5,
                3.7
        };

        // Рассчитаем моменты старта камеры
        List<Double> starts = instance.calcStartTimes(events, 1);

        // Вывод результата
        System.out.println(starts);
    }

    /*
     * Метод вычисляет моменты включения видеокамеры.
     *
     * Events - массив событий
     * workDuration - время работы камеры после запуска
     *
     * Алгоритм:
     * 1. Сортируем события
     * 2. Берём самое левое событие
     * 3. Включаем камеру в этот момент
     * 4. Пропускаем все события,
     *    которые попадают в интервал работы камеры
     */
    List<Double> calcStartTimes(double[] events, double workDuration) {

        // Список моментов включения камеры
        List<Double> result = new ArrayList<>();

        // Сортировка массива событий
        Arrays.sort(events);

        // Индекс текущего события
        int i = 0;

        // Пока есть необработанные события
        while (i < events.length) {

            // Время старта камеры
            double start = events[i];

            // Добавляем старт в результат
            result.add(start);

            // Время окончания работы камеры
            double end = start + workDuration;

            /*
             * Пропускаем все события,
             * которые камера успевает записать
             */
            while (i < events.length && events[i] <= end) {

                i++;
            }
        }

        // Возвращаем список стартов
        return result;
    }
}