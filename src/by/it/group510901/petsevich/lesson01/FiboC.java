package by.it.group510901.petsevich.lesson01;

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

    long getPisanoPeriod(long m)
    {
        long a = 0, b = 1;
        long period = 0;

        for(int i = 0; i < m * m; i++)
        {
            long tmp = a;
            a = b;
            b = (tmp + b) % m;

            if(a == 0 && b == 1) {
                period = i + 1;
                break;
            }
        }
        return period;
    }

    long fasterC(long n, int m) {
        long period = getPisanoPeriod(m);
        n = n % period;

        if(n == 0) return 0;
        if(n == 1) return 1;

        int a = 0, b = 1;
        for (long i = 2; i <= n; i++) {
            int tmp = a;
            a = b;
            b = (tmp + b) % m;
        }

        return b;
    }


}

