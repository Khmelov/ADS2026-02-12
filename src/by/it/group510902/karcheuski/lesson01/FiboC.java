package by.it.group510902.karcheuski.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секун
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
        if (n <= 1) return n;
        long prev = 0;
        long curr = 1;
        long period = 0;

        for (int i = 0; i < m * 6; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;

            if (prev == 0 && curr == 1) {
                period = i + 1;
                break;
            }
        }

        long index = n % period;
        if (index <= 1) return index;
        prev = 0;
        curr = 1;
        long res = 0;

        for (int i = 2; i <= index; i++) {
            res = (prev + curr) % m;
            prev = curr;
            curr = res;
        }
        return res;
    }


}

