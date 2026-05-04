package by.it.group551003.sologub.lesson01;

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
        int n = 999999999; // n = 55555;
        int m = 321;//m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        long period = 3;
        long newN;
        BigInteger[] arr = new BigInteger[2];
        arr[0] = BigInteger.ONE;
        arr[1] = BigInteger.TWO;
        BigInteger temp;
        BigInteger bigM = BigInteger.valueOf(m);
        while (!((arr[0].mod(bigM).equals(BigInteger.ZERO)) && (arr[1].mod(bigM).equals(BigInteger.ONE)))) {
            temp = arr[1];
            arr[1] = arr[0].add(arr[1]);
            arr[0] = temp;
            period++;
        }
        newN = n%period;
        arr[0] = BigInteger.ONE;
        arr[1] = BigInteger.ONE;
        for(int i = 0; i <= newN - 3; i++){
            temp = arr[1];
            arr[1] = arr[0];
            arr[0] = temp;
            arr[1] = arr[0].add(arr[1]);
        }

        return arr[1].mod(bigM).longValue();
    }


}