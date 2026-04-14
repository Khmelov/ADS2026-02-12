package by.it.group551001.belozorchik.lesson01;

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
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        if (n == 0 || n == 1)
            return n;
        if (m == 1)
            return 0;

        long x1 = 0;
        long x2 = 1;
        long x3;
        int ostx1;
        int ostx2; // начинаю с пары (0,1)
        int period = 0; // это длина периода

        // нахожу период
        do {
            x3 = x1 + x2;
            x1 = x2;
            x2 = x3;
            ostx1 = (int)(x1 % m);
            ostx2 = (int)(x2 % m);
            period++;
        } while (!(ostx1 == 0 && ostx2 == 1)); // пока не найдена (0,1) снова

        // period содержит длину периода Пизано
        // нахожу индекс k
        int k = (int)(n % period);

        // вычисляю F катое и возвращаю остаток
        if (k == 0) return 0;
        if (k == 1) return 1;

        long a = 0, b = 1, c = 0;       //где а = F(0), b = F(1)
        for (int i = 2; i <= k; i++) {
            c = a + b;
            a = b;
            b = c;
        }
        return c % m;
    }


}

