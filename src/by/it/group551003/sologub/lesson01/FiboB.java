package by.it.group551003.sologub.lesson01;

import java.math.BigInteger;
import java.util.ArrayList;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        //вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        BigInteger[] arr = new BigInteger[2];
        arr[0] = BigInteger.ZERO;
        arr[1] = BigInteger.ONE;
        if ((n == 0) || (n == 1))
            return BigInteger.valueOf(n);
        else{
            BigInteger temp;
            for(int i = 0; i <= n - 2; i++){
                temp = arr[1];
                arr[1] = arr[0];
                arr[0] = temp;
                arr[1] = arr[0].add(arr[1]);
            }
        }

        return arr[1];
    }

}
