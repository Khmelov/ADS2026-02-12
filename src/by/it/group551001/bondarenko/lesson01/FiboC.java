package by.it.group551001.bondarenko.lesson01;

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
        if (n <= 1) return n;
        int pr = 0;
        int cu = 1;
        int per = 0;
        for (int i = 0; i < 6 * m; i++) {
            int temp = cu;
            cu = (pr + cu) % m;
            pr = temp;
            if (pr == 0 && cu == 1) {
                per = i + 1;
                break;
            }
        }
        long lessN = n % per;
        if (lessN <= 1) return lessN;
        long fibPr = 0;
        long fibCu = 1;
        for (long i = 2; i <= lessN; i++) {
            long fibSled = (fibPr + fibCu) % m;
            fibPr = fibCu;
            fibCu = fibSled;
        }
        return fibCu;
    }


}

