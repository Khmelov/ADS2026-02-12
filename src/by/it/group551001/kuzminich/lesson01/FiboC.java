package by.it.group551001.kuzminich.lesson01;

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
        if (n==1) return 1;

        // search period of similar rests
        int prev_ost = 0;
        int next_ost = 1;
        int curr_ost;
        int count = 0;

        do{
            curr_ost = (prev_ost+next_ost) % m;
            prev_ost = next_ost;
            next_ost = curr_ost;
            count++;

        }while (!(prev_ost == 0 && next_ost == 1));


        long n_new = n % count;
        if (n_new == 0) return 0;
        if (n_new == 1) return 1;


        int first_slag = 0;
        int second_slag = 1;
        int new_slag = 0;
        for (long i = 2; i<=n_new; i++){
            new_slag = (first_slag+second_slag) % m;
            first_slag = second_slag;
            second_slag = new_slag;

        }
        return new_slag;


    }


}

