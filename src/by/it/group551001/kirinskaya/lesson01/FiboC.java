package by.it.group551001.kirinskaya.lesson01;

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
        int period = 0;
        int prev = 0;
        int curr = 1;

        do {
            int temp = curr;
            curr = (prev + curr) % m;
            prev = temp;
            period++;
        } while (!(prev == 0 && curr == 1));

        // Уменьшаем n с учетом периода
        long reducedN = n % period;

        // Вычисляем Fibonacci[reducedN] mod m
        if (reducedN <= 1) return reducedN;

        long fibPrev = 0;
        long fibCurr = 1;

        for (long i = 2; i <= reducedN; i++) {
            long fibNext = (fibPrev + fibCurr) % m;
            fibPrev = fibCurr;
            fibCurr = fibNext;
        }

        return fibCurr;

    }


}

