package by.it.group551002.liashkevich.lesson01;

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

        if (n == 0)
            return 0;
        if (n == 1)
            return 1;
        long mtrN[][] = {{0, 1}, {1, 1}};
        long temp[][] = new long[2][2];
        long ed[][] = {{1, 0}, {0, 1}};
        while (n > 0) {
            if ((n & 1) == 1) {
                for (int i = 0; i < mtrN.length; i++)
                    for (int k = 0; k < mtrN.length; k++) {
                        temp[i][k] = 0;
                        for (int j = 0; j < mtrN.length; j++)
                            temp[i][k] = (temp[i][k] + ed[i][j] * mtrN[j][k]) % m;
                    }
                for (int i = 0; i < mtrN.length; i++)
                    for (int j = 0; j < mtrN.length; j++)
                        ed[i][j] = temp[i][j];
            }
            for (int i = 0; i < mtrN.length; i++)
                for (int k = 0; k < mtrN.length; k++) {
                    temp[i][k] = 0;
                    for (int j = 0; j < mtrN.length; j++)
                        temp[i][k] = (temp[i][k] + mtrN[i][j] * mtrN[j][k]) % m;
                }
            for (int i = 0; i < mtrN.length; i++)
                for (int j = 0; j < mtrN.length; j++)
                    mtrN[i][j] = temp[i][j];
            n /= 2;
        }
        return ed[0][1];
    }


}

