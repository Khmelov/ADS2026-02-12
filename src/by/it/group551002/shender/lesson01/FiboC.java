package by.it.group551002.shender.lesson01;

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
        if (n<=1) return n;
        long prev = 0, curr = 1, temp = 1, period = 0;
        do {
            temp = prev;
            prev = curr;
            curr = (temp + curr) % m;
            period++;
        } while ((prev != 0) || (curr != 1));
        n = n % period;
        if (n <= 1) return n;
        prev = 0;
        curr = 1;
        for (int i = 2; i <= n; i++) {
            temp = prev;
            prev = curr;
            curr = (temp + curr) % m;
        }
        return curr;
    }


}

