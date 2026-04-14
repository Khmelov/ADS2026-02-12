package by.it.group510902.vinnichuk.lesson01;

import java.math.BigInteger;

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
        // Создаем массив для хранения чисел Фибоначчи до n включительно
        BigInteger[] f = new BigInteger[n + 1];

        // Базовые значения
        f[0] = BigInteger.ZERO;
        if (n > 0) {
            f[1] = BigInteger.ONE;
        }

        // Заполняем массив в цикле: каждое следующее число — сумма двух предыдущих
        // Сложность по времени O(n), по памяти O(n)
        for (int i = 2; i <= n; i++) {
            f[i] = f[i - 1].add(f[i - 2]);
        }

        return f[n];
    }
}

