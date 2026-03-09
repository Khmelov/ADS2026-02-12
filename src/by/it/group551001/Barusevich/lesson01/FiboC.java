package by.it.group551001.Barusevich.lesson01;

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

     public long fasterC(long n, int m) {

        if (m == 1) return 0;

        long a = 0, b = 1;
        int period = 0;
        int i = 0;

        while (i <= 6 * m && period == 0) {
            long c = (a + b) % m;
            a = b;
            b = c;

            if (a == 0 && b == 1) {
                period = i + 1;
            }
            i++;
        }

        if (period == 0) {
            period = 6 * m;
        }

            long r = n % period;

            if (r == 0) return 0;
            a = 0;
            b = 1;
            for (int j = 2; j <= r; j++) {
                long c = (a + b) % m;
                a = b;
                b = c;
            }
            return b;
        }
    }