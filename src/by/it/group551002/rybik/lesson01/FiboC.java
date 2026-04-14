package by.it.group551002.rybik.lesson01;

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

        if (n <= 1) {
            return n;
        }
        long[][] base = {{1, 1}, {1, 0}};

        long[][] res = mtxPow(base, n - 1, m);

        return res[0][0];
    }

    private long[][] mtxPow(long[][] mtx, long power, int m) {
        long[][] res ={{1, 0}, {0, 1}};

        while (power>0) {
            if (power % 2 == 1) {
                res = mtxMult(res, mtx, m);
            }
            mtx = mtxMult(mtx, mtx, m);
            power = power / 2;
        }
        return res;
    }

    private long[][] mtxMult(long[][] a, long[][] b, int m) {
        long[][] res = new long[2][2];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                res[i][j] = 0;
                for (int k = 0; k < 2; k++) {
                    res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % m;
                }
            }
        }
        return res;
    }

}

