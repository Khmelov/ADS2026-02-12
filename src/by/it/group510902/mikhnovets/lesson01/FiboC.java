package by.it.group510902.mikhnovets.lesson01;

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
        if (n <= 1) {
            return n;
        }

        int pisanoPeriod = findPisanoPeriod(m);

        long reducedN = n % pisanoPeriod;
        if (reducedN <= 1) {
            return reducedN;
        }

        long a = 0;
        long b = 1;
        long result = 0;

        for (long i = 2; i <= reducedN; i++) {
            result = (a + b) % m;
            a = b;
            b = result;
        }

        return result;
    }

    private int findPisanoPeriod(int m) {
        int a = 0;
        int b = 1;
        int c;
        for (int i = 0; i < m * m; i++) {
            c = (a + b) % m;
            a = b;
            b = c;
            if (a == 0 && b == 1) {
                return i + 1;
            }
        }

        return m * m;
        //return -1L;
    }


}

