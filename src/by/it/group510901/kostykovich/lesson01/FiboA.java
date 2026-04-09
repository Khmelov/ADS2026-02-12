package by.it.group510901.kostykovich.lesson01;

import java.math.BigInteger;
import java.util.Scanner;
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
        if(n<=0) return 0;
        if(n==1) return 1;
        int num1 = 0;
        int num2 = 1;
        for (int i = 2; i <= n; i++){
            int stoncks = num1 + num2;
            num1 = num2;
            num2 = stoncks;
        }
        return num2;
        //return 0;
    }


    BigInteger slowA(Integer n) {
        //рекурсия
        //здесь нужно реализовать вариант без ограничения на размер числа,
        //в котором код совпадает с математическим определением чисел Фибоначчи
        //время O(2^n)
        if(n <0) return BigInteger.ZERO;
        if(n == 1) return BigInteger.ONE;



return slowA(n-1).add(slowA(n-2));
        //return BigInteger.ZERO;
    }


}

