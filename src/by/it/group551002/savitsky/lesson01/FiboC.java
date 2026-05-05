package by.it.group551002.savitsky.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;

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

    long[][] multiply(long[][] a, long[][] b, int m){
        long[][] c = new long[2][2];

        c[0][0]= (a[0][0] * b[0][0] + a[0][1] * b[1][0]) % m;
        c[0][1]= (a[0][0] * b[0][1] + a[0][1] * b[1][1]) % m;
        c[1][0]= (a[1][0] * b[0][0] + a[1][1] * b[1][0]) % m;
        c[1][1]= (a[1][0] * b[0][1] + a[1][1] * b[1][1]) % m;

        return c;
    };

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации

     long[][] mat = {
             {1,1},
             {1,0}
     };

     long[][] res = {
             {1,0},
             {0,1}
     };

     while (n > 0) {
         if (n % 2 == 1) {
             res = multiply(res, mat, m);
         }
         mat = multiply(mat, mat, m);

         n = n / 2;
     }
        return res[0][1];
    }


}

