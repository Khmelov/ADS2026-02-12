package by.it.group551004.fedkovich.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {
    private long startTime = System.currentTimeMillis();
    private long[] cache;
    private long cacheSize;

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    public long pisanoPeriod(long m) {
        long a, b, c;

        a = 0;
        b = 1;
        c = a + b;

        for (int i = 0; i < m * 6; ++i) {
            c = (a + b) % m;
            a = b;
            b = c;

            if (a == 0 && b == 1) {
                return i + 1;
            }
        }

        return 0;
    }

    long fasterC(long n, int m) {
        // Интуитивно найти решение не всегда просто и
        // возможно потребуется дополнительный поиск информации
        long a, b, c;

        n %= pisanoPeriod(m);

        if (n < 2) {
            return n;
        }

        a = 0;
        b = 1;

        for (int i = 0; i < n - 1; ++i) {
            c = (a + b) % m;
            a = b;
            b = c;
        }

        return b;
    }

}
