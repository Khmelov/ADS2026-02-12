package by.it.group510902.vinnichuk.lesson01;

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
        // Простейший вариант рекурсии
        // Базовые случаи: F(0)=0, F(1)=1
        if (n < 2) {
            return n;
        }
        // Математическое определение: F(n) = F(n-1) + F(n-2)
        return calc(n - 1) + calc(n - 2);
    }



    BigInteger slowA(Integer n) {
        // Рекурсия с использованием BigInteger
        // Базовые случаи для BigInteger
        if (n == 0) {
            return BigInteger.ZERO;
        }
        if (n == 1) {
            return BigInteger.ONE;
        }

        // Вычисляем рекурсивно: сложение через .add()
        return slowA(n - 1).add(slowA(n - 2));
    }
}

