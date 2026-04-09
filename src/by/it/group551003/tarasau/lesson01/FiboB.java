package by.it.group551003.tarasau.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {
    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        BigInteger[] arr = new BigInteger[3];

        arr[0] = BigInteger.ZERO;
        arr[1] = BigInteger.ONE;
        arr[2] = BigInteger.ZERO;

        if (n == 0) {
            return arr[0];
        } else if (n == 1) {
            return arr[1];
        } else {
            for (int i = 0; i < n-1; i++) {
                arr[2] = arr[0].add(arr[1]);
                arr[0] = arr[1];
                arr[1] = arr[2];
            }

            return arr[2];
        }
    }

}
