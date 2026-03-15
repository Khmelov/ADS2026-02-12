package by.it.group551003.suchodolskaya.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

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

    //пизано
    long fasterC(long n, int m) {

        int pisanoPeriod = getPisanoPeriod(m);

        long reducedN = n % pisanoPeriod;

        return fibonacciMod(reducedN, m);
    }

    private int getPisanoPeriod(int m) {
        if (m == 1) return 1;

        int prev = 0;
        int current = 1;

        for (int i = 0; i < m * m; i++) {
            int next = (prev + current) % m;
            prev = current;
            current = next;

            if (prev == 0 && current == 1) {
                return i + 1; // Длина периода = i + 1
            }
        }

        return m * m;
    }

    private long fibonacciMod(long n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        long prev = 0;
        long current = 1;

        for (long i = 2; i <= n; i++) {
            long next = (prev + current) % m;
            prev = current;
            current = next;
        }

        return current;
    }

}

