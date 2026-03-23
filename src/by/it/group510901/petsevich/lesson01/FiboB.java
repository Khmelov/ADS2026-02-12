package by.it.group510901.petsevich.lesson01;

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
        BigInteger[] fibonacciNums = new BigInteger[n+1];

        fibonacciNums[0] = BigInteger.ZERO;
        fibonacciNums[1] = BigInteger.ONE;
        for(int i = 2; i <= n; i++)
            fibonacciNums[i] = fibonacciNums[i-1].add(fibonacciNums[i-2]);

        return fibonacciNums[n];
    }

}

