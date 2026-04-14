package by.it.group551001.sidorova.lesson01;
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
        return fib(n, m)[0];
    }

    // Быстрое удвоение: возвращает массив [F(n), F(n+1)] по модулю m
    private long[] fib(long n, long m) {
        if (n == 0)
            return new long[]{0, 1};

        long[] ab = fib(n / 2, m);
        long a = ab[0]; // F(k)
        long b = ab[1]; // F(k+1)

        long c = (a * ((2 * b % m - a + m) % m)) % m;      // F(2k)
        long d = ((a * a) % m + (b * b) % m) % m;          // F(2k+1)

        if (n % 2 == 0)
            return new long[]{c, d};
        else
            return new long[]{d, (c + d) % m};
    }



}