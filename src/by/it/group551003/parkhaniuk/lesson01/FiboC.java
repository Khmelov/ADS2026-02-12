package by.it.group551003.parkhaniuk.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.awt.image.AreaAveragingScaleFilter;
import java.nio.file.attribute.UserDefinedFileAttributeView;
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

    public static ArrayList<Integer> calcPisanoPeriod(long m)
    {
        ArrayList<Integer> sequance = new ArrayList<>();
        sequance.add(0);
        sequance.add(1);
        for(int i = 2; i <= 6 * m; i++)
        {
            sequance.add( (int) ( (sequance.get(i-1) + sequance.get(i-2)) % m ) );
            if (sequance.get(i-1) == 0 && sequance.get(i) == 1 && sequance.size() > 2)
            {
                sequance.removeLast();
                sequance.removeLast();
                return sequance;
            }
        }
        return sequance;
    }

    long fasterC(long n, int m) {
        ArrayList<Integer> period = calcPisanoPeriod(m);
        return period.get((int)n % period.size());
    }
}

