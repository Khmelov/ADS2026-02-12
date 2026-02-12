package by.it.group551004.bulavin.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        int prev = 0;
        int curr = 1;
        int period = 0;

        /*
         * Почитал, что такое период Пизано
         * и чекнул как люди накидывают алгоритм.
         * Постарался свою версию представить.
         * Был вариант написать через перемножение матриц,
         * Но я не математик. Сам к заданию полностью не додумался.
         */

        for (int i = 0; i < m * 6; i++) {
            int temp = (prev + curr) % m;
            prev = curr;
            curr = temp;

            if (prev == 0 && curr == 1) {
                period = i + 1;
            }
        }

        long reduced_n = n % period;

        long a = 0;
        long b = 1;
        long answer = 0;

        for (int i = 2; i <= reduced_n; i++) {
            answer = (a + b) % m;
            a = b;
            b = answer;
        }

        return answer;
    }
}

