package by.it.group510901.tsimoshenko.lesson01;

import javax.naming.Binding;
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
        BigInteger[] map = new BigInteger[n+1];
        map[0] = BigInteger.ZERO;
        map[1] = BigInteger.ONE;
        for (int i = 2; i <= n; i++){
            map[i] = map[i-1].add(map[i-2]);
        }
        return map[n];
    }

}

