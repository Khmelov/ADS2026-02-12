package by.it.group551004.yakhnin.lesson01;

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
        // Интуитивно найти решение не всегда просто
        if (n <= 1)
            return n % m;

        long time = 0;
        long a = 0;
        long b = 1;

        for (int i = 0; i < m * 6; i++) {
            long temp = (a + b) % m;
            a = b;
            b = temp;

            if (a == 0 && b == 1) {
                time = i + 1;
                break;
            }
        }

        long reducedN = n % time ;

        if (reducedN <= 1)
            return reducedN % m;

        long prev = 0;
        long cur = 1;
        //т.к.  числа меняют "c конца" следовательно при сложении важны только остатки
        for (int i = 0; i < reducedN - 1; i++) {
            long temp = (prev + cur) % m;
            prev = cur;
            cur = temp;
        }

        return cur;
    }


}

