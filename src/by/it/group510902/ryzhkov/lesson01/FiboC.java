package by.it.group510902.ryzhkov.lesson01;

import java.math.BigInteger;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

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
        // Интуитивно найти решение не всегда просто и
        // возможно потребуется дополнительный поиск информации
        if (m == 1)
            return 0L;
        if (n <= 1)
            return n % m;

        long p = 0L;
        long[] arr = new long[3];
        long r_n;
        long max = Math.min(n, 6L * m);

        arr[0] = 0L;
        arr[1] = 1L;

        for (long i = 2; i <= max; i++) {
            arr[2] = (arr[0] + (arr[1])) % m;

            if ((arr[1] == 0) && (arr[2] == 1)) {
                p = i - 1;
                break;
            }
            arr[0] = arr[1];
            arr[1] = arr[2];
        }
        if (p == 0) {
            p = 6L * m;
        }
        r_n = n % p;

        if (r_n == 0)
            return 0L;
        if (r_n == 1)
            return 1L % m;

        arr[0] = 0L;
        arr[1] = 1L;

        for (long i = 2; i <= r_n; i++) {
            arr[2] = (arr[0] + (arr[1])) % m;
            arr[0] = arr[1];
            arr[1] = arr[2];
        }

        return arr[2];
    }

}
