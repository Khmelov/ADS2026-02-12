package by.it.group510902.vinnichuk.lesson01;

import java.util.ArrayList;
import java.util.List;
/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = Math.toIntExact(10L);
        int m = 2;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {

        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        // Список для хранения последовательности остатков
        List<Long> cache = new ArrayList<>();
        cache.add(0L);
        cache.add(1L);

        // Ищем период Пизано
        // Период всегда начинается с 0 и 1
        for (int i = 2; i <= m * 6; i++) {
            cache.add((cache.get(i - 1) + cache.get(i - 2)) % m);

            // Если нашли начало следующего периода (0, 1)
            if (cache.get(i) == 1 && cache.get(i - 1) == 0) {
                // Длина периода равна i - 1
                long pisanoPeriod = i - 1;
                // Результат — это остаток в позиции (n % период)
                int index = (int) (n % pisanoPeriod);
                return cache.get(index);
            }
        }

        // Если n очень мало и период не успел завершиться
        return cache.get((int) n);
    }
}




