package by.it.group551003.guk;

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
        long result;
        int pisanoPeriod;
        long reducedN;
        long prev;
        long curr;
        long next;
        int i;

        if (n <= 1) {
            return n % m;
        }

        pisanoPeriod = calcPisanoPeriod(m);
        reducedN = n % pisanoPeriod;

        prev = 0;
        curr = 1;

        for (i = 2; i <= reducedN; i++) {
            next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        result = curr;
        return result;
    }

    private int calcPisanoPeriod(int m) {
        int period;
        int prev;
        int curr;
        int next;
        int i;

        prev = 0;
        curr = 1;

        for (i = 0; i < m * 6; i++) {
            next = (prev + curr) % m;
            prev = curr;
            curr = next;

            if (prev == 0 && curr == 1) {
                period = i + 1;
                return period;
            }
        }

        period = m * 6;
        return period;
    }

}

