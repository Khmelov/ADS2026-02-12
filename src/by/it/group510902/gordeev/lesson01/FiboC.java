package by.it.group510902.gordeev.lesson01;
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

        if (n == 0) return 0;
        if (n == 1) return 1 % m;

        ArrayList<Integer> period = getPisanoPeriod(m);
        int periodLength = period.size();


        int index = (int)(n % periodLength);


        return period.get(index);
    }


    private ArrayList<Integer> getPisanoPeriod(int m) {
        ArrayList<Integer> period = new ArrayList<>();
        period.add(0);
        period.add(1);

        int prev = 0;
        int curr = 1;


        for (int i = 0; i < m * 6; i++) {
            int next = (prev + curr) % m;
            period.add(next);

            prev = curr;
            curr = next;

            if (prev == 0 && curr == 1) {

                period.remove(period.size() - 1);
                period.remove(period.size() - 1);
                break;
            }
        }

        return period;
    }
}