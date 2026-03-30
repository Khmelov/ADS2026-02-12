package by.it.group551003.tarasau.lesson01;

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
        long pisanoPeriod = getPisanoPeriod(m);
        long prevN = 0;
        long currN = 1;
        long temp = 0;

        n = n % pisanoPeriod;

        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        }

        for (int i = 0; i < n - 1; i++) {
            temp = currN;
            currN = (prevN + currN) % m;
            prevN = temp;
        }

        return currN % m;
    }

    static long getPisanoPeriod(long m) {
        long prevN = 0;
        long currN = 1;
        long pisanoPeriod = 0;
        long temp = 0;

        for (int i = 0; i < m * m; i++) {
            temp = currN;
            currN = (prevN + currN) % m;
            prevN = temp;

            if ((prevN == 0) && (currN == 1)) {
                pisanoPeriod = i + 1;
            }
        }

        return pisanoPeriod;
    }

}

