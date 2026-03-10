package by.it.group551002.tereshchenko.lesson01;

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

    BigInteger pred = BigInteger.ZERO;
    BigInteger curr = BigInteger.ONE;
    BigInteger fastB(Integer n) {
        BigInteger[] Arr = new BigInteger[n+1];
        for (int i = 0; i <= n; i++) {
            if  (i == 1) {
                Arr[1]=BigInteger.ONE;
                continue;
            }
            if (i == 0) {
                Arr[0]=BigInteger.ZERO;
                continue;
            }
            Arr[i] = Arr[i-1].add(Arr[i-2]);
        }
        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        return Arr[n];
    }

}

