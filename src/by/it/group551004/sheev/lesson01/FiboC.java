package by.it.group551004.sheev.lesson01;

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
        int last;
        int curr;
        int period;
        int temp;
        last = 0;
        curr = 1;
        period = 0;

        if (n <= 1) {
            return n;
        }
        if (m == 1) {
            return 0;
        }

        for (int i = 0; i < m * 6; i++) {
            temp = curr;
            curr = (last + curr) % m;
            last = temp;

            if (last == 0 && curr == 1) {
                period = i + 1;
                break;
            }
        }
        n = n % period;

        last = 0;
        curr = 1;

        for (long i = 2; i <= n; i++) {
            temp = curr;
            curr = (last + curr) % m;
            last = temp;
        }

        return curr;
    }
}

