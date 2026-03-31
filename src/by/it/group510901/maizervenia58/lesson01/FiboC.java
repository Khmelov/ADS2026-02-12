package by.it.group510901.maizervenia58.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        long n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d,%d)=%d \n\t time=%d ms\n\n", n, m, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    // Быстрое вычисление n-го числа Фибоначчи по модулю m
    public long fasterC(long n, int m) {
        if (n <= 1) return n % m;

        // Используем Pisano period для уменьшения n
        int pisano = getPisanoPeriod(m);
        n = n % pisano;

        // Вычисляем F(n) по модулю m циклом
        long a = 0;
        long b = 1;
        for (long i = 1; i < n; i++) {
            long c = (a + b) % m;
            a = b;
            b = c;
        }
        return b % m;
    }

    // Нахождение периода Писано для модуля m
    private int getPisanoPeriod(int m) {
        int prev = 0;
        int curr = 1;
        for (int i = 0; i < m * 6; i++) { // период <= 6*m
            int next = (prev + curr) % m;
            prev = curr;
            curr = next;
            if (prev == 0 && curr == 1) return i + 1;
        }
        return -1; // на случай ошибки
    }
}

