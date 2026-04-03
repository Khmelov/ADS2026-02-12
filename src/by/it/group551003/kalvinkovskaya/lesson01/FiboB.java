package by.it.group551003.kalvinkovskaya.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        //вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        int i;
        BigInteger[] fNumbs;
        if (n <= 1)
            return BigInteger.valueOf(n);
        fNumbs = new BigInteger[n];
        fNumbs[0] = BigInteger.ZERO;
        fNumbs[1] = BigInteger.ONE;
        for (i = 2; i < n; i++) {
            fNumbs[i] = fNumbs[i - 1].add(fNumbs[i - 2]);
        }
        return fNumbs[i - 1].add(fNumbs[i - 2]);
    }

}

