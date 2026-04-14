package by.it.group551002.kuzmenia.lesson01;

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

    int getPeriodPisano(int m) {
        short prev = 0, curr = 1;
        int period = 0;

        for (int i = 0; i < 6 * m; i++) {
            short next = (short) ((prev + curr) % m);
            prev = curr;
            curr = next;

            if (prev == 0 && curr == 1) {
                period = i + 1;
                break;
            }
        }

        return period;
    }

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации

        // получаем период Пизано для заданного m
        int periodPisano = getPeriodPisano(m);

        // вычисляем остаток n от полученного периода (номер в периоде)
        long modN = n % periodPisano;

        // обработка базовых случаев
        if (modN == 0) {
            return 0;
        }
        if (modN == 1) {
            return 1 % m;
        }

        // считаем modN-ое число Фибоначчи и возвращаем остаток от его деления на m
        long prev = 0;
        long curr = 1;
        for (int i = 2; i <= modN; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }
        return curr;
    }


}

