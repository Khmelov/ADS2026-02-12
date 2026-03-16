package by.it.group510902.omelkovich.lesson01;

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
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        if (n <= 1) return n;
        long prev = 0;
        long current = 1;
        long temp = 0;

        for (int i = 0; i < m * 6; i++) {
            long next = (prev + current) % m;
            prev = current;
            current = next;

            if (prev == 0 && current == 1) {
                temp = i + 1;
                break;
            }
        }

        long index = n % temp;
        if (index <= 1) return index;
        prev = 0;
        current = 1;
        long res = 0;

        for (int i = 2; i <= index; i++) {
            res = (prev + current) % m;
            prev = current;
            current = res;
        }
        return res;
    }

}

