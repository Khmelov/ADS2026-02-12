package by.it.group510901.chernyavskaya.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.util.ArrayList;
import java.util.List;

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
        long curr = 1;
        long period = 0;

        do {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
            period++;
        } while (!(prev == 0 && curr == 1));

        long newN = n % period;

        if (newN <= 1) return newN;

        prev = 0;
        curr = 1;
        for (int i = 2; i <= newN; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        return curr;
    }


}

