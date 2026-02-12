package by.it.group551004;

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

        int period = findPisanoPeriod(m);
        long remainder = n % period;
        return fibonacciMod(remainder, m);
    }

    private int findPisanoPeriod(int m) {
        int prev = 0;
        int curr = 1;
        int period = 0;
        boolean found = false;

        for (int i = 0; i < m * 6 && !found; i++) {
            int temp = curr;
            curr = (prev + curr) % m;
            prev = temp;

            if (prev == 0 && curr == 1) {
                period = i + 1;
                found = true;
            }
        }

        return period;
    }

    private long fibonacciMod(long n, int m) {
        if (n <= 1) {
            return n;
        }

        long prev = 0;
        long curr = 1;

        for (int i = 2; i <= n; i++) {
            long temp = curr;
            curr = (prev + curr) % m;
            prev = temp;
        }

        return curr;
    }
}