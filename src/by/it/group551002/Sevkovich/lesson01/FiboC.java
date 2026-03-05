package by.it.group551002.Sevkovich.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 10;
        int m = 10;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        // Юзаю теорему Пизано из инета
        // Период Пизано всегда <= 6m
        int[] seqPisano = new int[6*m+2];
        seqPisano[0] = 0;
        seqPisano[1] = 1;
        long seqLength = 0;
        for (int i = 2; i < seqPisano.length; i++){
            seqPisano[i] = (seqPisano[i-1] + seqPisano[i-2]) % m;
            if (seqPisano[i] == 1 && seqPisano[i-1] == 0) {seqLength = i-1; break;}
        }

        return seqPisano[(int)(n % seqLength)];
    }


}

