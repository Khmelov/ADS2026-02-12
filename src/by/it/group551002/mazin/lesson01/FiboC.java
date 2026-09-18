package by.it.group551002.mazin.lesson01;

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
        if (n <= 1) return n % m;

        long period = 0;
        long a = 0;
        long b = 1;

        for (long i = 0; i < (long) m * 6; i++) {
            long temp = (a + b) % m;
            a = b;
            b = temp;
            if (a == 0 && b == 1) {
                period = i + 1;
                break;
            }
        }

        long targetIndex = n % period;
        if (targetIndex <= 1) return targetIndex;

        long res = 0;
        long prev = 0;
        long curr = 1;

        for (long i = 1; i < targetIndex; i++) {
            res = (prev + curr) % m;
            prev = curr;
            curr = res;
        }

        return curr;
    }


}

