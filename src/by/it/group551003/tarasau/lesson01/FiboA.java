package by.it.group551003.tarasau.lesson01;

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

        //вычисление чисел фибоначи медленным методом (рекурсией)
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
        //здесь простейший вариант, в котором код совпадает
        //с математическим определением чисел Фибоначчи
        //время O(2^n)
        int xPrev = 0;
        int xCurr = 1;
        int xNext = 0;

        if (n == 0)
            xNext = 0;
        else if (n == 1)
            xNext = 1;
        else
            for (int i = 0; i < n-1; i++) {
                xNext = xPrev + xCurr;
                xPrev = xCurr;
                xCurr = xNext;
            }

        return xNext;
    }


    BigInteger slowA(Integer n) {
        //рекурсия
        //здесь нужно реализовать вариант без ограничения на размер числа,
        //в котором код совпадает с математическим определением чисел Фибоначчи
        //время O(2^n)
        BigInteger first = BigInteger.ZERO;
        BigInteger second = BigInteger.ONE;

        if (n == 1) {
            return first;
        } else if (n == 2) {
            return second;
        } else {
            return recursiveFibonachi(first, second, n-1);
        }
    }


    BigInteger recursiveFibonachi(BigInteger n1, BigInteger n2, Integer iterCount) {
        BigInteger nextN = BigInteger.ZERO;

        nextN = n1.add(n2);
        n1 = n2;
        n2 = nextN;

        --iterCount;

        if (iterCount != 0) {
            nextN = recursiveFibonachi(n1, n2, iterCount);
        }

        return nextN;
    }

}