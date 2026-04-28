package by.it.group551001.evtushenko.lesson01;

import java.util.ArrayList;

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
        
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(0);
        a.add(1);

        int i;
        for(i = 0;;++i){
            a.add((a.get(a.size() - 1) + (a.get(a.size() - 2))) % m);

            if(a.get(a.size() - 1) == 0 && a.get(a.size() - 2) == 1) break;
        }

        return a.get((int) (n % (i + 2)));
    }

}

