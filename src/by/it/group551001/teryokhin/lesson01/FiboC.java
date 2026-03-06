package by.it.group551001.teryokhin.lesson01;

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
        int limit = (int)1e5;
        BigInteger m_BI = BigInteger.valueOf(m);

        BigInteger[] arr = new BigInteger[limit + 1];
        BigInteger[] mods = new BigInteger[3];

        mods[0] = arr[0] = BigInteger.ZERO;
        mods[1] = arr[1] = BigInteger.ONE;

        for(int i = 2;i <= limit;i++)
        {
            arr[i] = arr[i - 2].add(arr[i - 1]);

            mods[2] = arr[i].mod(m_BI);

            if(i > 2 && mods[0].equals(BigInteger.ZERO) && mods[1].equals(BigInteger.ONE) && mods[2].equals(BigInteger.ONE))
            {
                return arr[(int)(n % (i - 2))].mod(m_BI).longValue();
            }

            mods[0] = mods[1];
            mods[1] = mods[2];
        }

        return -1L;
    }


}

