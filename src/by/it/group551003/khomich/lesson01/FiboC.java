package by.it.group551003.khomich.lesson01;

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
        long prev = 0;
        long curr = 1;
        long period = getPisanoPeriod(m);
        long r = n % period;

        if (r == 0) return 0;
        if (r == 1) return 1;

        for (long i = 2; i <= r; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }
        return curr;
    }

    static long getPisanoPeriod(int m) {
        long prev = 0;
        long curr = 1;
        long periodPisano = 0;
        long temp;

        for (int i = 0; i < 6 * m && periodPisano == 0; i++) {
            temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
            if (prev == 0 && curr == 1) {
                periodPisano = i + 1;
            }
        }

        if (periodPisano == 0) {
            periodPisano = 6 * m;
        }

        return periodPisano;
    }
}
