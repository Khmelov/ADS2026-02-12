package by.it.group551004.kunikin.lesson01;

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
        long periodPisano = 0, a = 0, b = 1;
        for (int i = 0; i < 6 * m; i++) {
            long temp = (a + b) % m;
            a = b;
            b = temp;
            if (a == 0 && b == 1) {
                periodPisano = i + 1;
                break;
            }
        }
        long targetIndex = n % periodPisano;
        long resA = 0, resB = 1;
        for (int i = 0; i < targetIndex; i++) {
            long temp = (resA + resB) % m;
            resA = resB;
            resB = temp;
        }
        return resA;
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
    }


}

