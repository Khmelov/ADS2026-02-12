package by.it.group551001.biarezina.lesson01;

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

        boolean changed = false;
        long a = 0, c, b = 1;

        long  pred = 0, curr = 1, period = 0;
        for (int i = 0; i < 6 * m; i++){
            long temp = curr;
            curr = (pred + temp) % m;
            pred = temp;

            if (pred == 0 && curr == 1){
                period = i + 1;
                break;
            }
        }

        long red_n = (n % period);

        if (n == 0) return 0L;
        if (n == 1) return 1L;
        for (int i = 2; i <= (int) red_n + 1; i++){
            c = b;
            b = (c + a) % m;
            a = c;

        }

        return a;

        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации

    }


}

