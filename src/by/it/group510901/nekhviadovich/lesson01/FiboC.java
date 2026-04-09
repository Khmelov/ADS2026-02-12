package by.it.group510901.nekhviadovich.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;
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
        if(n < 1){ return 0L; }
        if(n == 1) { return 1L; }
        long reducedN = n%getPisano(m);//n in m range

        ArrayList<Long> fiboNumbers = new ArrayList<>();
        fiboNumbers.add(0L);
        fiboNumbers.add(1L);
        for (int i = 2; i <= reducedN; i++){
            fiboNumbers.add(fiboNumbers.get(i-2) + fiboNumbers.get(i-1));
        }
        long nFibo = fiboNumbers.get((int)reducedN);
        return nFibo % m;
    }

    int getPisano(int m){

        int prev = 0;
        int curr = 1;

        for(int i = 0; i < m * 10; i++){ // m*6 is actual maximum
            if(prev == 0 && curr == 1 && i > 0){ return i; }
            int nextMod = (prev + curr) % m;
            prev = curr;
            curr = nextMod;
        }
        return 0;
    }




}

