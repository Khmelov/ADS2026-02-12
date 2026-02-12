package by.it.group551001.romanov.lesson1;

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
        if (n == 1) return 1 % m;
        if (n == 2) return 1 % m;

        long[][] matrix = { {1, 1}, {1, 0} };
        long[][] result = { {1, 0}, {0, 1} };
        n = n - 1;

        while (n > 0) {
            if ((n & 1) == 1) {
                result = multiply(result, matrix, m);
            }
            matrix = multiply(matrix, matrix, m);
            n >>= 1;
        }

        return result[0][0];
    }

    static long[][] multiply(long[][] a, long[][] b, long m) {
        long[][] result = new long[2][2];

        result[0][0] = (a[0][0] * b[0][0] % m + a[0][1] * b[1][0] % m) % m;
        result[0][1] = (a[0][0] * b[0][1] % m + a[0][1] * b[1][1] % m) % m;
        result[1][0] = (a[1][0] * b[0][0] % m + a[1][1] * b[1][0] % m) % m;
        result[1][1] = (a[1][0] * b[0][1] % m + a[1][1] * b[1][1] % m) % m;

        return result;
    }

}

