package by.it.group551002.dovgash.lesson01;

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
        if (n<2) return BigInteger.valueOf(n);

        BigInteger[] arr = new BigInteger[2];
        arr[0] = BigInteger.valueOf(0);
        arr[1] = BigInteger.valueOf(1);

        for (int i = 2; i <= n; i++){
            BigInteger temp = arr[0].add(arr[1]);
            arr[0] = arr[1];
            arr[1] = temp;
        }
        return arr[1];
        //return BigInteger.valueOf(-1L);
    }

}

