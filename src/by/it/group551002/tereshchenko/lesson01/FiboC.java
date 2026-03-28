package by.it.group551002.tereshchenko.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.util.HashMap;
import java.util.Map;

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
        long pred = 0;
        long curr = 1;
        long per = 0;
        long next = 0;
        long lim = (long) m * (long) m;
        for (long i = 0; i < lim; i++) {
            next = (pred + curr) % m;
            pred = curr;
            curr = next;
            if ((pred == 0) && (curr == 1)) {
                per = i + 1;
                break;
            }
        }
        pred = 0;
        curr = 1;
            long a = n % per;
            for (int k = 2; k <= a; k++) {
                next = (pred + curr) % m;
                pred = curr;
                curr = next;
            }
            //Интуитивно найти решение не всегда просто и
            //возможно потребуется дополнительный поиск информации
            return curr;
        }



}


