package by.it.group551002.voroshko.lesson01;

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

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        if (n < 2) return n % m;
        long[][] tMatr = {{1, 1}, {1, 0}};
        long[][] fibMatr = powMatr(tMatr, n - 1, m);
        return fibMatr[0][0];
    }

    long[][] mulMatr(long[][] arrA, long[][] arrB, int m) {
        long[][] arrC = {{0, 0}, {0, 0}};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    long res = (arrA[i][k] % m) * (arrB[k][j] % m);
                    arrC[i][j] = (arrC[i][j] + res) % m;
                }
            }
        }
        return arrC;
    }

    long[][] powMatr(long[][] arrA, long n, int m) {
        long[][] arrRes = {{1, 0}, {0, 1}};
        while (n > 0) {
            if (n % 2 == 1)
                arrRes = mulMatr(arrRes, arrA, m);
            arrA = mulMatr(arrA, arrA, m);
            n /= 2;
        }
        return arrRes;
    }

}