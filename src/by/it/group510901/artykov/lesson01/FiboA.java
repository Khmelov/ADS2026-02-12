package by.it.group510901.artykov.lesson01;

import java.math.BigInteger;

/*
 * Класс для вычисления чисел Фибоначчи.
 * Реализованы:
 * 1) Обычная рекурсия через int
 * 2) Рекурсия через BigInteger для больших чисел
 */

public class FiboA {

    // Переменная для измерения времени выполнения
    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {

        FiboA fibo = new FiboA();

        int n = 33;

        // Вызов обычного рекурсивного метода
        System.out.printf(
                "calc(%d)=%d \n\t time=%d \n\n",
                n,
                fibo.calc(n),
                fibo.time()
        );

        // Вызов рекурсивного метода с BigInteger
        fibo = new FiboA();

        n = 34;

        System.out.printf(
                "slowA(%d)=%d \n\t time=%d \n\n",
                n,
                fibo.slowA(n),
                fibo.time()
        );
    }

    /*
     * Метод для подсчёта времени выполнения программы.
     * Возвращает время в миллисекундах.
     */
    private long time() {

        long res = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();

        return res;
    }

    /*
     * Рекурсивный метод вычисления числа Фибоначчи.
     *
     * Формула:
     * F(n) = F(n-1) + F(n-2)
     *
     * Базовые случаи:
     * F(0) = 0
     * F(1) = 1
     *
     * Используется тип int,
     * поэтому подходит только для небольших чисел.
     */
    private int calc(int n) {

        // Базовый случай
        if (n <= 1) {
            return n;
        }

        // Рекурсивный вызов
        return calc(n - 1) + calc(n - 2);
    }

    /*
     * Рекурсивный метод вычисления чисел Фибоначчи
     * без ограничения размера числа.
     *
     * Используется BigInteger,
     * поэтому можно вычислять очень большие числа.
     */
    BigInteger slowA(Integer n) {

        // Базовый случай: F(0) = 0
        if (n == 0) {
            return BigInteger.ZERO;
        }

        // Базовый случай: F(1) = 1
        if (n == 1) {
            return BigInteger.ONE;
        }

        // Рекурсивное вычисление:
        // F(n) = F(n-1) + F(n-2)
        return slowA(n - 1).add(slowA(n - 2));
    }
}