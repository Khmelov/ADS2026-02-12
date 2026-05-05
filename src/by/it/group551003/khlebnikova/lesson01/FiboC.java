package by.it.group551003.khlebnikova.lesson01;
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

    public static ArrayList<Integer> findPisanoPeriod(int m) {
        ArrayList<Integer> sequence = new ArrayList<>();
        int prevNum = 0;
        int currNum = 1;
        sequence.add(0);
        if (m == 1) {
            return sequence;
        }
        sequence.add(1);
        for (int i = 2; i <= m * 6 + 2; ++i) {
            int next = (prevNum + currNum) % m;
            sequence.add(next);
            prevNum = currNum;
            currNum = next;
            if (prevNum == 0 && currNum == 1 && sequence.size() > 2) {
                sequence.removeLast();
                sequence.remove(sequence.size() - 2);
                return sequence;
            }
        }
        return sequence;
    }

    long fasterC(long n, int m) {
        ArrayList<Integer> period = findPisanoPeriod(m);
        return period.get((int) (n % period.size()));
    }


}

