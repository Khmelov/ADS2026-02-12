package by.it.group510901.gulchenko.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.util.ArrayList;

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
        long prev = 0;
        long curr = 1;
        int period = 0;

        for (int i = 2; i <= 6 * m; i++) {
            long temp = curr;
            curr = (curr + prev) % m;
            prev = temp;

            if (prev == 0 && curr == 1) {
                period = i - 1;
                break;
            }
        }

        long targetIndex = n % period;

        if(targetIndex <= 1) return targetIndex;

        long findPrev = 0;
        long findCurr = 1;
        for(int i = 0; i < targetIndex - 1; i++){
            long temp = findCurr;
            findCurr = (findCurr + findPrev) % m;
            findPrev = temp;
        }

        return findCurr;

    }
}

