package by.it.group551002.loiko.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    private int findPisanoPeriod(int m) {
        // Базовый случай
        if (m == 1) {
            return 1;
        }

        long prev = 0;
        long curr = 1;
        int period = 0;

        for (int i = 0; i < m * m; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;

            // Период начинается с 0,1 и повторяется
            if (prev == 0 && curr == 1) {
                period = i + 1;
                break;
            }
        }
        return period;
    }

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации

        // Тривиальные случаи
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1 % m;
        }

        // Находим период Пизано для модуля m
        int period = findPisanoPeriod(m);

        // Уменьшаем n с помощью периода
        long remainder = n % period;

        // Вычисляем Fib(remainder) mod m
        if (remainder == 0) {
            return 0;
        }
        if (remainder == 1) {
            return 1 % m;
        }

        long prev = 0;
        long curr = 1;
        for (int i = 2; i <= remainder; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }
        return curr % m;
    }
}