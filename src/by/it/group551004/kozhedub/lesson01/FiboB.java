package by.it.group551004.kozhedub.lesson01;

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
        BigInteger[] fiboNums = new BigInteger[n + 1];

        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;

        fiboNums[0] = new BigInteger("0");
        fiboNums[1] = new BigInteger("1");

        for (int i = 2; i <= n; i++) {
            fiboNums[i] = fiboNums[i-1].add(fiboNums[i-2]);
        }
        return fiboNums[n];
    }

}

