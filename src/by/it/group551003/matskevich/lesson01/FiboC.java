package by.it.group551003.matskevich.lesson01;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        by.it.group551003.matskevich.lesson01.FiboC fibo = new by.it.group551003.matskevich.lesson01.FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        if (n == 1)
            return n % m;
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

        long remainder = n % period;

        if (remainder <= 1)
            return remainder;

        prev = 0;
        curr = 1;

        for (int i = 2; i <= remainder; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        return curr;
    }
}
