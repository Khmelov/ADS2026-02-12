package by.it.group551001.chaley.lesson1;

import java.math.BigInteger;

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        BigInteger fib[] = new BigInteger[n];
        fib[0] = BigInteger.ONE;
        fib[1] = BigInteger.ONE;
        for (int i = 2; i < n; i++)
            fib[i] = fib[i-1].add(fib[i-2]);
        return fib[n-1];
    }
}

