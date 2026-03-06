package by.it.group551002.Sevkovich.lesson01;

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
        if (n==1) return BigInteger.ONE;
        BigInteger[] fibSequence = new BigInteger[n];
        fibSequence[0] = BigInteger.ONE;
        fibSequence[1] = BigInteger.ONE;
        BigInteger a=BigInteger.ONE, b=BigInteger.ONE, temp;
        for (int i = 2; i < n; i++){
            fibSequence[i] = fibSequence[i-1].add(fibSequence[i-2]);
        }
        return fibSequence[n-1];
    }

}

