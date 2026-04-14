package by.it.group510902.manko.lesson01;

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
        long[] array = new long[10 * m];
        array[1] = 1;
        int i;
        for(i = 2; i <= n; i++){
            array[i] = (array[i - 1] + array[i - 2]) % m;
            if(array[i] == array[1] && array[i - 1] == array[0]) break;
        }
        return array[(int)(n % i)];
    }


}

