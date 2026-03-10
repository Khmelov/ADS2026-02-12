package by.it.group551003.kravchenkovikk.lesson01;

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
        int number1, number2;
        int sum;
        number1 = 0;
        number2 = 1;
        sum = 0;
        for (int i = 2; i <= n; i++) {
            sum = number1 + number2;
            number1 = number2;
            number2 = sum;
        }
        return number2;
    }


    BigInteger slowA(Integer n) {
        if (n <= 1)
            return BigInteger.valueOf(n);
        else
            return slowA(n - 1).add(slowA(n - 2));
    }


}

