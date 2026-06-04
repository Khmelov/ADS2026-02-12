package by.it.group510901.artykov.lesson01;

import java.math.BigInteger;

/*
 * Класс для быстрого вычисления чисел Фибоначчи.
 *
 * Используется:
 * - вспомогательный массив
 * - тип BigInteger для очень больших чисел
 *
 * Сложность:
 * Время: O(n)
 * Память: O(n)
 */

public class FiboB {

    // Переменная для измерения времени выполнения
    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {

        // Создание объекта класса
        FiboB fibo = new FiboB();

        int n = 55555;

        // Вызов быстрого метода вычисления
        System.out.printf(
                "fastB(%d)=%d \n\t time=%d \n\n",
                n,
                fibo.fastB(n),
                fibo.time()
        );
    }

    /*
     * Метод для вычисления времени выполнения программы.
     * Возвращает время в миллисекундах.
     */
    private long time() {

        return System.currentTimeMillis() - startTime;
    }

    /*
     * Быстрое вычисление числа Фибоначчи.
     *
     * Используется массив, где:
     * arr[i] = i-е число Фибоначчи
     *
     * Формула:
     * F(n) = F(n-1) + F(n-2)
     *
     * Базовые случаи:
     * F(0) = 0
     * F(1) = 1
     *
     * Используется BigInteger,
     * поэтому ограничений на размер числа нет.
     */
    BigInteger fastB(Integer n) {

        // Базовый случай
        if (n == 0) {
            return BigInteger.ZERO;
        }

        // Создание массива для хранения чисел Фибоначчи
        BigInteger[] arr = new BigInteger[n + 1];

        // Начальные значения
        arr[0] = BigInteger.ZERO;
        arr[1] = BigInteger.ONE;

        // Заполнение массива
        for (int i = 2; i <= n; i++) {

            arr[i] = arr[i - 1].add(arr[i - 2]);
        }

        // Возвращаем n-е число Фибоначчи
        return arr[n];
    }
}