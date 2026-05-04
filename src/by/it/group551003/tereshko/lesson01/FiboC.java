package by.it.group551003.tereshko.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;

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

    long[][] matrixMultiply(long[][] a, long[][] b, int m) {
        return new long[][]{
                {(a[0][0] * b[0][0] + a[0][1] * b[1][0]) % m,
                        (a[0][0] * b[0][1] + a[0][1] * b[1][1]) % m},
                {(a[1][0] * b[0][0] + a[1][1] * b[1][0]) % m,
                        (a[1][0] * b[0][1] + a[1][1] * b[1][1]) % m}
        };
    }

    long fibIter(long n, int m) {
        long[][] result = {{1, 0}, {0, 1}};
        long[][] fibMatrix = {{1, 1}, {1, 0}};
        while (n > 0) {
            if (n % 2 == 1) result = matrixMultiply(result, fibMatrix, m);
            fibMatrix = matrixMultiply(fibMatrix, fibMatrix, m);
            n /= 2;
        }
        return result[0][1];
    }

    long fasterC(long n, int m) {
        if (n == 0) return 0L;
        if (n == 1) return 1L;
        return fibIter(n, m);
    }
}


