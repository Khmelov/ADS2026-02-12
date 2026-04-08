package by.it.group510901.filimonova.lesson01;

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
        int pisanoPeriod = getPisanoPeriod(m);
        long reducedN = n % pisanoPeriod;
        return fibMod(reducedN, m);
    }
    private int getPisanoPeriod(int m) {
        int prev = 0;
        int curr = 1;
        int period = 0;

        for (int i = 0; i < m * 6; i++) { // Период не превышает 6m
            int temp = curr;
            curr = (prev + curr) % m;
            prev = temp;

            if (prev == 0 && curr == 1) {
                period = i + 1;
                break;
            }
        }
        return period;
    }
    private long fibMod(long n, int m) {
        if (n <= 1) {
            return n;
        }

        long prev = 0;
        long curr = 1;

        for (long i = 2; i <= n; i++) {
            long temp = curr;
            curr = (prev + curr) % m;
            prev = temp;
        }

        return curr;
    }


}

