package by.it.group551003.dobrolovich.lesson01;

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
        BigInteger r = BigInteger.ONE;
        BigInteger a1 = BigInteger.ONE;
        BigInteger a2 = BigInteger.ONE;
        if (n < 1) return BigInteger.ZERO;
        if (n < 3) return BigInteger.ONE;
        for (int i = 2; i < n; i++) {
            a1 = a2;
            a2 = r;
            r = a1.add(a2);
        }
        return r;
    }

}

