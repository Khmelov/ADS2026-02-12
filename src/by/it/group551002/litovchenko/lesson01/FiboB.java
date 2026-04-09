package by.it.group551002.litovchenko.lesson01;

import java.math.BigInteger;
import java.util.ArrayList;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        // вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        if (n == 0)
            return BigInteger.ZERO;
        if (n == 1)
            return BigInteger.ONE;

        BigInteger[] fibs_nums = new BigInteger[n + 1];
        fibs_nums[0] = BigInteger.ZERO;
        fibs_nums[1] = BigInteger.ONE;

        for (int i = 2; i <= n; i++) {
            fibs_nums[i] = fibs_nums[i - 1].add(fibs_nums[i - 2]);
        }

        return fibs_nums[n];
    }

}
