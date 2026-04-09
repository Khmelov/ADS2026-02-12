package by.it.group551002.kubarko.lesson01;

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

        ArrayList<Integer> l = new ArrayList<>();
        l.add(0);
        l.add(1);

        for(int i = 2; i < 6*m; i++)
        {
            l.add( (l.get(i-1) + l.get(i - 2)) % m);
            if(l.get(i) == 1 && l.get(i-1) == 0)
            {
                n %= i-1;
                return l.get((int)n);
            }

        }
        return l.get((int)n);
    }


}

