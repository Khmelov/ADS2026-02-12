<<<<<<<< HEAD:src/by/it/group510901/rusilevich/lesson01/FiboB.java
package by.it.group510901.rusilevich.lesson01;
========
package by.it.group551001.mousik_aleksey.lesson01;
>>>>>>>> 7330764456c1f8a303ac12bd1d01ce62bc18cfeb:src/by/it/group551001/mousik_aleksey/lesson01/FiboB.java

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
<<<<<<<< HEAD:src/by/it/group510901/rusilevich/lesson01/FiboB.java
        if (n < 0) {
            throw new IllegalArgumentException("n не может быть отрицательным");
        }
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }
========
        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        int[] Mas = new int [n];
        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;
>>>>>>>> 7330764456c1f8a303ac12bd1d01ce62bc18cfeb:src/by/it/group551001/mousik_aleksey/lesson01/FiboB.java
        BigInteger[] fib = new BigInteger[n + 1];
        fib[0] = BigInteger.ZERO;
        fib[1] = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1].add(fib[i - 2]);
        }

        return fib[n];
        //return BigInteger.valueOf(-1L);
    }

}

