package by.it.group551003.stupakov.lesson01;

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
        int period=2;
        int n_reduced;
        int[] fib=new int[6*m+2];
        boolean notFound=true;
        if (m==1) return 0;
        if (n<=1) return n;
        fib[0]=0;
        fib[1]=1;
        do
        {
            fib[period]=(fib[period-1]+fib[period-2])%m;
            if (fib[period]%m==0 && fib[period-1]%m==1)
                notFound = false;
            else
                ++period;
        }while (notFound);
        n_reduced=(int)n%period;
        return fib[n_reduced];
    }


}

