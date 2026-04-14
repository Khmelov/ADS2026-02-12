package by.it.group551004.fedoruk.lesson01;

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
        long[] fib = new long[6 * m];
        int i = 2;
        boolean flag = true;
        if (n < 2) return n;
        fib[0] = 0;
        fib[1] = 1;

        while (flag) {
            fib[i] = (fib[i - 1] + fib[i - 2]) % m;
            if (fib[i - 1] == 0 && fib[i] == 1)
                flag = false;
            else i++;
        }
        return fib[(int) (n % (i-1))];
    }


}

