package by.it.group551002.brutski.lesson01;
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
        if (n == 0)
            return BigInteger.ZERO;
        if (n == 1)
            return BigInteger.ONE;

        ArrayList<BigInteger> fib = new ArrayList<>();
        BigInteger last;
        fib.add(BigInteger.ZERO);
        fib.add(BigInteger.ONE);

        for (int i = 2; i <= n; i++) {
            last = BigInteger.ZERO;
            last = last.add(fib.get(i - 1));
            last = last.add(fib.get(i - 2));
            fib.add(last);
        }
        return fib.getLast();
    }

}

