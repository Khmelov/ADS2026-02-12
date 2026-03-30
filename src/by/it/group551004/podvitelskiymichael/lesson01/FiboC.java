package by.it.group551004.podvitelskiymichael.lesson01;

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
        if (n == 0) return 0;
        if (n == 1 || n == 2) return 1 % m;

        // Находим период Пизано π(m)
        int a = 0, b = 1;
        long period = 0;
        do {
            int temp = (a + b) % m;
            a = b;
            b = temp;
            period++;
        } while (!(a == 0 && b == 1));

        // Вычисляем F(n % period) mod m
        long k = n % period;
        if (k == 0) k = period;

        long prev = 0, curr = 1;
        for (long i = 2; i <= k; i++) {
            long temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
        }
        return curr;
    }


}

