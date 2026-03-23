package by.it.group551004.kondratova.lesson01;

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
        // Special case: everything mod 1 is 0
        if (m == 1) {
            return 0;
        }

        // Find the Pisano period for modulus m
        int period = getPisanoPeriod(m);

        // Reduce n using the period property: F(n) mod m = F(n % period) mod m
        n = n % period;

        // Compute F(n) mod m iteratively for the reduced n
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }

        long prev = 0;
        long curr = 1;
        for (long i = 2; i <= n; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        return curr;
    }

    private int getPisanoPeriod(int m) {
        // The sequence of Fibonacci numbers modulo m is periodic (Pisano period)
        // It always starts with (0, 1). We search for the next occurrence of this pair.
        long prev = 0;
        long curr = 1;

        // Theoretical upper bound: Pisano period π(m) ≤ 6*m
        for (int length = 1; length <= 6 * m; length++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;

            // When we encounter (0, 1) again, a full period has completed
            if (prev == 0 && curr == 1) {
                return length;
            }
        }

        // Fallback (should never be reached for valid m ≥ 2)
        return 6 * m;
    }
}