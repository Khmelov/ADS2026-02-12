package by.it.group551004.pedko.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;

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

        int arrFib[] = new int[6 * m + 2];
        int i, answer;
        long range;
        answer = 1;

        if (n + 1 > 6 * m + 2)
            range = 6 * m + 2;
        else
            range = n + 1;

        arrFib[0] = 0;
        arrFib[1] = 1;

        for (i = 2; i <= range; i++) {
            answer = (arrFib[i - 1] + arrFib[i - 2]) % m;
            if ((answer == 1) && (arrFib[i - 1] == 0))
                return arrFib[Math.toIntExact(n % (i - 1))];
            arrFib[i] = answer;
        }

        return answer;
    }


}

