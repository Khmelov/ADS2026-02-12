package by.it.group551001.mousik_aleksey.lesson01;

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
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        long period=0;
        long pred=0;
        long curr=1;
        int i = 0;
        while (i < m * m && !(pred == 0 && curr == 1 && period > 0)) {
            long next = (pred + curr) % m;
            pred = curr;
            curr = next;
            period++;
            i++;
        }
        long reduced = n % period;
        if (reduced == 0) return 0;
        if (reduced == 1) return 1 % m;
        long a = 0; long b = 1;
        for (long k = 2; k <= reduced; k++) {
            long c = (a + b) % m;
            a = b;
            b = c;
        }
        return b;
        //return -1L;
    }


}

