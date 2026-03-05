package by.it.group510901.dezinskaa.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private final long startTime = System.currentTimeMillis();

    static void main(String... args) {
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
        if (m==1) return 0;

        int period =0;
        int prev = 0;
        int curr = 1;

        for (int i=0; i<m*6; i++)
        {
            int next = (prev + curr) % m;
            prev = curr;
            curr = next;

            if (prev == 0 && curr ==1)
            {
                period = i + 1;
                break;
            }
        }

        long nMod = n % period;



        if (nMod == 0) return 0;
        if (nMod == 1) return 1;

        long a = 0;
        long b = 1;
        for (long i = 2; i<= nMod; i++)
        {
            long c = (a+b)% m;
            a=b;
            b=c;
        }
        return b;
    }


}

