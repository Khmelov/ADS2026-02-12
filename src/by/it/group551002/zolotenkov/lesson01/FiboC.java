package by.it.group551002.zolotenkov.lesson01;

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
        if (n == 0)
            return 0;
        if (n == 1)
            return 1;
        int[] seq = new int[6 * m];
        seq[0] = 0;
        seq[1] = 1;
        int first = 0, second = 1, third = 1, period = 1, i = 2;

        System.out.printf("%3d%3d", first, second);
        do {
            third = (first + second) % m;
            System.out.printf("%3d", third);
            first = second;
            second = third;
            seq[i] = third;
            period++;
            i++;
        } while (first != 0 || second != 1);
        period -= 2;
        System.out.printf("\n%3d\n", period);
        return seq[(int) n % (period + 1)];
    }


}


