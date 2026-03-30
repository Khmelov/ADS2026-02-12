package by.it.group551003.hirzhon.lesson01;

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

    static long[][] matrixMultiply(long[][] a, long[][] b, int m) {
        long[][] r = new long[2][2];
        r[0][0] = (a[0][0] * b[0][0] + a[0][1] * b[1][0]) % m;
        r[0][1] = (a[0][0] * b[0][1] + a[0][1] * b[1][1]) % m;
        r[1][0] = (a[1][0] * b[0][0] + a[1][1] * b[1][0]) % m;
        r[1][1] = (a[1][0] * b[0][1] + a[1][1] * b[1][1]) % m;
        return r;
    }

    private long[][] exponentation(long[][] mtx, long n, int m) {
        long[][] r ={{1, 0}, {0, 1}};

        while (n > 0) {
            if (n % 2 == 1)
                r = matrixMultiply(r, mtx, m);
            mtx = matrixMultiply(mtx, mtx, m);
            n = n / 2;
        }
        return r;
    }

    long fasterC(long n, int m) {
        long[][] matrix = { {1, 1}, {1, 0} };
        if (n == 0) return 0L;
        if (n == 1) return 1 % m;
        if (n == 2) return 1 % m;

        long[][] p = exponentation(matrix, n - 1, m);
        return p[0][0] % m;
    }


}

