package by.it.group551001.telipko.lesson01;

import java.math.BigInteger;
import java.util.ArrayList;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private final ArrayList<BigInteger> foundedNumbers;
    private long startTime = System.currentTimeMillis();

    public FiboB() {
        foundedNumbers = new ArrayList<>();
        foundedNumbers.add(BigInteger.ZERO);
        foundedNumbers.add(BigInteger.ONE);
    }

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

        int lastIndex = foundedNumbers.size()-1;

        if (n>lastIndex) {
            BigInteger prev = foundedNumbers.get(lastIndex-1);
            BigInteger last = foundedNumbers.get(lastIndex);
            BigInteger res;
            for (int i = lastIndex; i < n; i++) {
                res = last.add(prev);
                foundedNumbers.add(res);
                prev = last;
                last = res;
            }
        }

        return foundedNumbers.get(n);
    }

}

