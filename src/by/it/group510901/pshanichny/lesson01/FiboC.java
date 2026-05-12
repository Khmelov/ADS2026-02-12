package by.it.group510901.pshanichny.lesson01;

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
        // Шаг 1: найти период Пизано для m
        // Период всегда начинается с 0, 1 и не превышает m*m
        long[] pisano = new long[2];
        pisano[0] = 0;
        pisano[1] = 1;
        long period = 0;

        for (long i = 0; i < (long) m * m; i++) {
            long next = (pisano[0] + pisano[1]) % m;
            pisano[0] = pisano[1];
            pisano[1] = next;

            // Период найден когда последовательность снова равна 0, 1
            if (pisano[0] == 0 && pisano[1] == 1) {
                period = i + 1;
                break;
            }
        }

        // Шаг 2: свести n к индексу внутри периода
        long remainder = n % period;

        // Шаг 3: вычислить fib(remainder) % m обычной итерацией
        if (remainder == 0) return 0;
        if (remainder == 1) return 1;

        long prev = 0, curr = 1;
        for (long i = 2; i <= remainder; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        return curr;
    }
}