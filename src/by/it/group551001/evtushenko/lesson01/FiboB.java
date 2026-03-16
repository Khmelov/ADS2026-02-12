package by.it.group551001.evtushenko.lesson01;

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

        ArrayList<BigInteger> a = new ArrayList<BigInteger>();
        a.add(BigInteger.ONE);
        a.add(BigInteger.ONE);

        while (a.size() != n) {
            a.add(a.get(a.size() - 1).add(a.get(a.size() - 2)));
        }
        return a.get(a.size() - 1);
    }

}

