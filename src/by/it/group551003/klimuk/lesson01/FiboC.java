package by.it.group551003.klimuk.lesson01;

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
        long x1 = 0, x2 = 1, i = 0, period = 1;
        boolean found = false;
        while (i < m * 6 && !found) {
            long temp = (x1 + x2) % m;
            x1 = x2;
            x2 = temp;

            if (x1 == 0 && x2 == 1) {
                period = i + 1;
                found = true;
            }
            i++;
        }
        x1 = 0;
        x2 = 1;
        n %= period;
        for (i = 2; i <= n; i++) {
            long temp = (x1 + x2) % m;
            x1 = x2;
            x2 = temp;
        }
        return x2;
    }


}

