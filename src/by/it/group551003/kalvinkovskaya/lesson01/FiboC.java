package by.it.group551003.kalvinkovskaya.lesson01;

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
        int i = 0;
        int pisanoPeriod = 0;
        long n1 = 0;
        long n2 = 1;
        long newN;
        long temp;
        boolean notFound;
        notFound = true;
        while ((i < m * 6) && (notFound)) {
            temp = (n1 + n2) % m;
            n1 = n2;
            n2 = temp;
            if ((n1 == 0) && (n2 == 1)) {
                notFound = false;
                pisanoPeriod = i + 1;
            }
            i++;
        }
        newN = n % pisanoPeriod;
        if (newN <= 1)
            return newN;
        n1 = 0;
        n2 = 1;
        for (i = 2; i <= newN; i++) {
            temp = (n1 + n2) % m;
            n1 = n2;
            n2 = temp;
        }
        return n2;
    }


}

