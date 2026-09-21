package by.it.group510901.artykov.lesson01;

/*
 * Даны:
 * 1 <= n <= 1E18
 * 2 <= m <= 1E5
 *
 * Необходимо найти:
 * остаток от деления n-го числа Фибоначчи на m
 *
 * Используется:
 * - период Пизано
 * - сложность O(m * m)
 *
 * Решение работает очень быстро
 * даже для огромных n.
 */

public class FiboC {

    // Переменная для измерения времени выполнения
    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {

        FiboC fibo = new FiboC();

        int n = 55555;
        int m = 1000;

        // Вызов быстрого метода
        System.out.printf(
                "fasterC(%d)=%d \n\t time=%d \n\n",
                n,
                fibo.fasterC(n, m),
                fibo.time()
        );
    }

    /*
     * Метод для вычисления времени выполнения.
     */
    private long time() {

        return System.currentTimeMillis() - startTime;
    }

    /*
     * Метод вычисляет:
     * F(n) mod m
     *
     * Используется период Пизано.
     *
     * Идея:
     * Остатки чисел Фибоначчи при делении на m
     * начинают повторяться.
     *
     * Повторение начинается с:
     * 0 1
     *
     * После нахождения периода:
     * n уменьшается:
     * n = n % period
     *
     * Затем обычным циклом
     * вычисляется остаток.
     */
    long fasterC(long n, int m) {

        // Базовые случаи
        if (n <= 1) {
            return n;
        }

        long previous = 0;
        long current = 1;

        long period = 0;

        /*
         * Поиск периода Пизано
         *
         * Максимальная длина периода:
         * m * m
         */
        for (int i = 0; i < m * m; i++) {

            long temp = current;

            current = (previous + current) % m;

            previous = temp;

            // Если снова получили 0 1,
            // значит период найден
            if (previous == 0 && current == 1) {

                period = i + 1;

                break;
            }
        }

        // Уменьшаем n
        n = n % period;

        // Если после уменьшения получили 0 или 1
        if (n <= 1) {
            return n;
        }

        // Повторное вычисление Fibonacci mod m
        previous = 0;
        current = 1;

        for (long i = 2; i <= n; i++) {

            long temp = current;

            current = (previous + current) % m;

            previous = temp;
        }

        return current;
    }
}