package by.it.group510902.slesar.lesson01;

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

    long fasterC(int n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        if (n <= 1) return n % m;

        long[] fibost = new long[(m*m+1)*2];

        fibost[0] = 0;
        fibost[1] = 1;

        int period = 0;

        for (int i = 2; i < fibost.length; i++) {
            fibost[i] = (fibost[i - 1] + fibost[i - 2]) % m;

            if (fibost[i - 1] == 0 && fibost[i] == 1) {
                period = i - 1;
                break;
            }
        }

        if (period > 0) {
            n = n % period;
        }

        return fibost[n];
    }
}