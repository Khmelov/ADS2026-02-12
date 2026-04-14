package by.it.group510901.zuev.lesson01;

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
        int period = 0;
        int a = 0;
        int b = 1;

        while (true) {
            int c = (a + b) % m;
            a = b;
            b = c;
            period++;
            if (a == 0 && b == 1) {
                break;
            }
        }
        int k = (int) (n % period);
        if (k == 0) {
            return 0;
        }

        int prev = 0;
        int curr = 1;
        for (int i = 2; i <= k; i++) {
            int next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }
        return curr % m;
    }


}

