package by.it.group510901.maizervenia58.lesson01;

import java.math.BigInteger;

public class FiboA {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboA fibo = new FiboA();
        int n = 33;
        System.out.printf("calc(%d)=%d \n\t time=%d \n\n", n, fibo.calc(n), fibo.time());

        // вычисление чисел Фибоначчи медленным методом (рекурсией)
        fibo = new FiboA();
        n = 34;
        System.out.printf("slowA(%d)=%d \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());
    }

    private long time() {
        long res = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        return res;
    }

    // простой вариант int (быстро, но переполняется на больших n)
    public int calc(int n) {
        if (n <= 1) return n;
        return calc(n - 1) + calc(n - 2);
    }

    // медленный вариант через BigInteger (рекурсия)
    public BigInteger slowA(Integer n) {
        if (n <= 1) return BigInteger.valueOf(n);
        return slowA(n - 1).add(slowA(n - 2));
    }
}




