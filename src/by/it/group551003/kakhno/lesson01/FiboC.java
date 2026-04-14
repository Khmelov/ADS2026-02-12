package by.it.group551003.kakhno.lesson01;

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
        if (n < 2) return n;
        int pisanoPeriod = 0, lim = 6 * m, size = lim + 2; // п(m)<=6m - период Пизано не превышает 6m
        int[] pisanoArr = new int[size];
        pisanoArr[0] = 0;
        pisanoArr[1] = 1;
        for (int i = 2; i < lim; i++) {
            pisanoArr[i] = (pisanoArr[i - 1] + pisanoArr[i - 2]) % m;
            if (pisanoArr[i - 1] == 0 && pisanoArr[i] == 1) { // нумерация с нуля, если iый эл-т =0(новое начало, новый нулевой), то чисел в периоде i
                pisanoPeriod = i - 1;
                break;
            }
        }
        // F[n] % m = F[n % п[m]] % m
        int reducedN = (int) (n % pisanoPeriod); // (int) - преобразуем long в int
        return pisanoArr[reducedN];
    }


}

