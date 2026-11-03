package by.it.group510901.rusilevich;

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
        if (m == 1) return 0;
        if (n <= 1) return n % m;
        long a = 0;
        long b = 1;
        long period = 0;
        for (long i = 2; i <= 6L * m; i++) {
            long c = (a + b) % m;
            a = b;
            b = c;
            if (a == 0 && b == 1) {
                period = i - 1;
                break;
            }
        }
        if (period == 0) {
            period = 6L * m;
        }
        long reducedN = n % period;
        if (reducedN == 0) return 0;
        if (reducedN == 1) return 1;
        long f0 = 0;
        long f1 = 1;
        long fn = 0;
        for (int i = 2; i <= reducedN; i++) {
            fn = (f0 + f1) % m;
            f0 = f1;
            f1 = fn;
        }
        return fn;
    }


}

