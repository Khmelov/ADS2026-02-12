package by.it.group551001.docenko.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;
import java.util.ArrayList;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 100;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        ArrayList<Long> arr = new ArrayList<>();
        BigInteger x1, x2, x;
        long mod_1, mod_2;

        mod_1 = mod_2 = 1L;
        x1 = x2 = BigInteger.ONE;

        arr.add(0L);
        arr.add(1L);
        arr.add(1L);

        if (n == 0)
            return 0;
        else if (n < 3)
            return 1;

        while (mod_1 != 0 || mod_2 != 1) {
            x = x1.add(x2);
            x1 = x2;
            x2 = x;

            mod_1 = mod_2;
            mod_2 = x.mod(BigInteger.valueOf(m)).longValue();

            arr.add(mod_2);
            System.out.println(x);
        }

        return arr.get((int) n % (arr.size()-2));
    }

}

