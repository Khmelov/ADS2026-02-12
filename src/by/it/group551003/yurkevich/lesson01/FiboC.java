package by.it.group551003.yurkevich.lesson01;
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
        if (m == 1) return 0;
        ArrayList<Integer> digits = new ArrayList<>();
        int prev = 0, curr = 1;
        digits.add(0);
        digits.add(1);
        for (int i = 2; i <= 6 * m + 2; i++){
            digits.add((prev + curr) % m);
            int temp = (prev + curr) % m;
            prev = curr;
            curr = temp;

            if (prev == 0 && curr == 1 && digits.size() > 2){
                digits.removeLast();
                break;
            }
        }
        return digits.get((int)(n % digits.size()));
    }
}

