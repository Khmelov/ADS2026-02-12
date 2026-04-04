package by.it.group551004.haritonov.lesson01;

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

    long fasterC(long n, int m) {
        if (m == 1) return 0;
        long a = 0, b = 1;
        long period = 0;
        do {
            long c = (a + b) % m;
            a = b;
            b = c;
            period++;
        } while (!(a == 0 && b == 1));
        long k = n % period;
        if (k == 0) return 0;
        if (k == 1) return 1 % m;
        long f0 = 0, f1 = 1;
        for (long i = 2; i <= k; i++) {
            long f2 = (f0 + f1) % m;
            f0 = f1;
            f1 = f2;
        }

        return f1;
    }


}

