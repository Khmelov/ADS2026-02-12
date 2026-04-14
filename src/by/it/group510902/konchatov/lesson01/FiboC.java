package by.it.group510902.konchatov.lesson01;

import java.math.BigInteger;

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
        if (n<=1) return n % m;
        int a=0, b=1,period=0;
        for (int p=0; p<m*m;p++) {
            int c=(a+b)%m;  
            a=b;
            b=c;
            if (a == 0 && b == 1) {
                period =p+1;
                break;
            }
        }
        long r = n % period;
        if (r== 0) return 0;
        if (r== 1) return 1 % m;
        long prev = 0;
        long curr = 1;
        for (long i=2; i<=r; i++) {
            long next =prev+curr;
            prev = curr;
            curr = next;
        }
        
        return curr%m;
    }


}

