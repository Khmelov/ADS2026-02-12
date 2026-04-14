package by.it.group551004.kozhedub.lesson01;

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
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        long index = n % getPisanoPeriod(m);

        return getFiboNumber(index) % m;
    }

    long getFiboNumber(long n) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        long prev = 0;
        long cur = 1;
        long tmp;

        for (int i = 2; i <= n; i++) {
            tmp = cur;
            cur = prev + cur;
            prev = tmp;
        }

        return cur;
    }

    long getPisanoPeriod(int m) {
        int prev = 0;
        int cur = 1;
        int tmp;

        for (int i = 0; i < m * m; i++) {
            tmp = (cur + prev) % m;
            prev = cur;
            cur = tmp;

            if (prev == 0 & cur == 1) {
                return i + 1;
            }
        }
        return 0;
    }

}

