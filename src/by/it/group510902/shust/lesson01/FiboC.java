package by.it.group510902.shust.lesson01;

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

    long fasterC(long n, int m) {
        // 1. Ищем длину периода Пизано
        long periodLength = 0;
        long prev = 0;
        long curr = 1;

        // Максимальная длина периода Пизано для m не превышает 6*m
        for (int i = 0; i < m * 6; i++) {
            long temp = (prev + curr) % m;
            prev = curr;
            curr = temp;

            // Период начинается с 0 и 1
            if (prev == 0 && curr == 1) {
                periodLength = i + 1;
                break;
            }
        }

        // 2. Находим остаток от деления n на длину периода
        long remainder = n % periodLength;
        if (remainder == 0) return 0;

        // 3. Вычисляем нужное число Фибоначчи для полученного остатка
        prev = 0;
        curr = 1;
        for (int i = 1; i < remainder; i++) {
            long temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
        }

        return curr % m;
    }


}

