package by.it.group510902.strekha.lesson01;

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
        long initial = 0;
        long next = 1;
        long period = 0;

        for (int i = 2; i <= 6 * m; i++) {
            long temp = next;
            next = (initial + next) % m;
            initial = temp;

            if (initial == 0 && next == 1) {
                period = i - 1;
                break;
            }
        }

        long targetIndex = n % period;

        if(targetIndex <= 1) return targetIndex;

        long startValue = 0;
        long nextValue = 1;
        for (int i = 0; i < targetIndex - 1; i++) {
            long temp = nextValue;
            nextValue = (startValue + nextValue) % m;
            startValue = temp;
        }

        return nextValue;
    }
}

