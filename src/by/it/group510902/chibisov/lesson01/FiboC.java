package by.it.group510902.chibisov.lesson01;

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
        long a = 0L;
        long b = 1L;
        long pisano = 0;
        for (int i = 2; i <= 6 * m; i++)
        {
            long temp = a;
            a = b;
            b = (temp + b) % m;
            if (a == 0 && b == 1)
            {
                pisano = i - 1;
                break;
            }
        }
        if (pisano != 0)
        {
            n %= pisano;
            if (n == 0) return a;
            for (int i = 2; i <= n; i++)
            {
                long temp = a;
                a = b;
                b = (temp + b) % m;
            }
        }
        return b;
    }


}

