package by.it.group510901.gulchenko.lesson01;

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

        BigInteger[] f = new BigInteger[n + 1];
        f[0] = BigInteger.ZERO;
        f[1] = BigInteger.ONE;

        for (int i = 2; i <= n; i++) {
            f[i] = f[i - 2].add(f[i - 1]);
        }

        return f[n];
    }

}

