package by.it.group551001.kuzminich.lesson01;

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
        //здесь простейший вариант, в котором код совпадает
        //с математическим определением чисел Фибоначчи
        //время O(2^n)

        int first_slag = 0;
        int second_slag = 1;
        int new_slag = 0;
        for (int i = 2; i<=n; i++){
            new_slag = first_slag+second_slag;
            first_slag = second_slag;
            second_slag = new_slag;

        }
        return new_slag;
    }


    BigInteger slowA(Integer n) {
        //рекурсия
        //здесь нужно реализовать вариант без ограничения на размер числа,
        //в котором код совпадает с математическим определением чисел Фибоначчи
        //время O(2^n)

        if (n == 0) {
            return BigInteger.ZERO;  // F(0) = 0
        }
        if (n == 1) {
            return BigInteger.ONE;   // F(1) = 1
        }

        // ТОЛЬКО ПОСЛЕ базовых случаев - рекурсия
        return slowA(n - 1).add(slowA(n - 2));
    }


}

