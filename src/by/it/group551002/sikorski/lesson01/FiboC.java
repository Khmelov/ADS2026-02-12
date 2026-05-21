package by.it.group551002.sikorski.lesson01;

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
        // Находим период Пизано для модуля m
        long period = getPisanoPeriod(m);

        n = n % period;

        if (n <= 1) return n;

        long prev = 0;
        long curr = 1;
        long res = n;

        for (int i = 2; i <= n; i++) {
            res = (prev + curr) % m;
            prev = curr;
            curr = res;
        }

        return res;
    }

    private long getPisanoPeriod(int m) {
        long prev = 0;
        long curr = 1;
        long res = 0;

        for (int i = 0; i < m * 6; i++) {
            long temp = curr;
            curr = (prev + curr) % m;
            prev = temp;

            if (prev == 0 && curr == 1) {
                return i + 1;
            }
        }
        return res;
    }


}

