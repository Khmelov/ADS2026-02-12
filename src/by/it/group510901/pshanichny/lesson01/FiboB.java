package by.it.group510901.pshanichny.lesson01;

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
        ArrayList<BigInteger> fibonacci = new ArrayList<>();
        fibonacci.add(BigInteger.ZERO);
        fibonacci.add(BigInteger.ONE);

    for (int index = 0; index < n; index++) {
        int size = fibonacci.size();
        BigInteger num1 = fibonacci.get(size - 1);
        BigInteger num2 = fibonacci.get(size - 2);
        BigInteger result = num1.add(num2);
        fibonacci.add(result);
    }

        return fibonacci.get(n);
    }

}

