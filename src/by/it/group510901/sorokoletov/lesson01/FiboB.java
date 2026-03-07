package by.it.group510901.sorokoletov.lesson01;

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
        if (n <= 1) return BigInteger.valueOf(n);

        BigInteger prev = BigInteger.ZERO; // 0
        BigInteger curr = BigInteger.ONE;  // 1

        for (int i = 2; i <= n; i++) {
            BigInteger next = prev.add(curr);
            prev = curr;
            curr = next;
        }

        return curr;

    }

}

