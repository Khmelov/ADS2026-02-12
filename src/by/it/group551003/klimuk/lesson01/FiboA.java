package by.it.group551003.klimuk.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить рекурсивный способ вычисления чисел Фибоначчи
 */

public class FiboA {


    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboA fibo = new FiboA();
        int n = 33;
        System.out.printf("calc(%d)=%d \n\t time=%d \n\n", n, fibo.calc(n), fibo.time());

        //вычисление чисел фибоначчи медленным методом (рекурсией)
        fibo = new FiboA();
        n = 34;
        System.out.printf("slowA(%d)=%d \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());
    }

    private long time() {
        long res = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        return res;
    }

    private int calc(int n) {
        int x1 = 0, x2 = 1, x3;
        for (int i = 0; i < n - 1; i++) {
            x3 = x1 + x2;
            x1 = x2;
            x2 = x3;
        }
        return x1;
    }

    BigInteger slowA(Integer n) {
        if(n == 0){
            return BigInteger.ZERO;
        }
        if(n == 1){
            return BigInteger.ONE;
        }
        return slowA(n-1).add(slowA(n-2));
    }


}