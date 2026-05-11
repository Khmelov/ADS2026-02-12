package by.it.group510902.nebyshynets.lesson01;

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
        BigInteger temp1 = new BigInteger("0");
        BigInteger temp2 = new BigInteger("1");
        BigInteger fibo = new BigInteger("0");
        if(n == 0) return BigInteger.ZERO;
        if(n == 1) return BigInteger.ONE;
        for(Integer i = 1;i<n;i++){
            fibo = temp1.add(temp2);
            temp1 = temp2;
            temp2 = fibo;
        }
        return fibo;
    }

}

