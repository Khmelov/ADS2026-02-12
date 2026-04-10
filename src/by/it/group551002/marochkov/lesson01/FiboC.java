package by.it.group551002.marochkov.lesson01;

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
        if (n < 2) return n % m;
        long[][] tMat = {{1, 1}, {1, 0}};
        long[][] fibMat = powMat(tMat, n - 1, m);
        return fibMat[0][0];
    }

    long[][] mulMatr(long[][] A, long[][] B, int m) {
        long[][] C = {{0, 0}, {0, 0}};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    long res = (A[i][k] % m) * (B[k][j] % m);
                    C[i][j] = (C[i][j] + res) % m;
                }
            }
        }
        return   C;
    }

    long[][] powMat(long[][] A, long n, int m) {
        long[][] arrRes = {{1, 0}, {0, 1}};
        while (n > 0) {
            if (n % 2 == 1)
                arrRes = mulMatr(arrRes, A, m);
            A = mulMatr(A, A, m);
            n /= 2;
        }
        return arrRes;
    }



}

