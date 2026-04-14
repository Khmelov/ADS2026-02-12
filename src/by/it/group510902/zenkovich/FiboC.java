package by.it.group510902.zenkovich;

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
        if (n <= 1) {
            return n % m;
        }


        long pisanoPeriod = getPisanoPeriod(m);


        long reducedN = n % pisanoPeriod;


        if (reducedN <= 1) {
            return reducedN % m;
        }

        long prev = 0;
        long curr = 1;

        for (long i = 2; i <= reducedN; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        return curr % m;
    }

    private long getPisanoPeriod(int m) {
        long prev = 0;
        long curr = 1;
        long period = 0;

        for (long i = 0; i < m * 6L; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;

            if (prev == 0 && curr == 1) {
                period = i + 1;
                break;
            }
        }

        return period;
    }


}

