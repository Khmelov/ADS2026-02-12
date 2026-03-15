package by.it.group551004.sharkevich.lesson01;

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
        int a, b, c, period;
        long reducedN, prev, curr, next;

        if (n < 2) return n;

        a = 0;
        b = 1;
        period = 0;

        for (int i = 0; i < m * 6; i++) {
            c = (a + b) % m;
            a = b;
            b = c;
            if (a == 0 && b == 1) {
                period = i + 1;
                break;
            }
        }

        reducedN = n % period;

        if (reducedN < 2) return reducedN;

        prev = 0;
        curr = 1;
        reducedN++;

        for (long i = 2; i < reducedN; i++) {
            next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        return curr;

    }


}

