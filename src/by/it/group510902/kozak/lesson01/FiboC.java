package by.it.group510902.kozak.lesson01;

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
        return fib(n,m)[0];
    }
    /*
     * F(2k)=F(k)∗(2F(k+1)−F(k)) F(2k+1)=F(k)^2+F(k+1)^2
     *
     */
    private long[] fib(long n, long m) {
        if (n==0) return new long[] {0,1};
        long[] half = fib(n/2, m);
        long fk = half[0];
        long fk1 = half[1];
        long f2k = fk * ((2*fk1-fk+m)%m) %m;
        long f2k1 = (fk*fk+fk1*fk1) % m;
        if (n%2 == 0) return new long[]{f2k, f2k1};
        else return new long[]{f2k1, (f2k+f2k1) % m};
    }



}

