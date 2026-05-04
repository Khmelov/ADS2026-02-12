package by.it.group551002.bohonmaxim.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.util.ArrayList;
import java.util.List;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        List<Long> pixels = new ArrayList<>();
        pixels.add(0L);
        pixels.add(1L);
        for (int i = 2; i <= m * 6; i++) {
            pixels.add((pixels.get(i - 1) + pixels.get(i - 2)) % m);
            if (pixels.get(i) == 1 && pixels.get(i - 1) == 0) {
                long periodLength = i - 1;
                int reducedN = (int) (n % periodLength);
                return pixels.get(reducedN);
            }
        }

        return pixels.get((int) n);
    }


}

