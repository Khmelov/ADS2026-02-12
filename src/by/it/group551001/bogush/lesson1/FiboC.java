package by.it.group551001.bogush.lesson1;

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
        if (n == 0 || n == 1)
            return n;
        if (m == 1)
            return 0;
        long x1 = 0;
        long x2 = 1;
        long x3;
        int ox1,ox2;
        int l = 1;
        do {
          x3 = x1 + x2;
          l++;
          x1 = x2;
          ox1 = (int)(x1 % m); // Остаток члена фибоначи на m
          x2 = x3;
          ox2 = (int)(x2 % m);  // Остаток члена фибоначи на m
        } while (ox1 != 0 || ox2 != 1);
        /*Период Пизано повторится,когда остатки членов последовательности Фибоначчи
        станет снова равно 0 и 1 ( так как в начале 0 mod m = 0 и 1 mod m = 1 для m > 1)*/
        int Pizano_Period = l - 1; // Сам период Пизано (Его длина)
        //Найдём номер первого числа ,стоящего на том же месте где и F(n) в периоде Пизано
        int FiboNumber = (int)(n % Pizano_Period);
        long[] Arr = new long[FiboNumber + 1];
        Arr[0] = 0;
        Arr[1] = 1;
        int i;
        for (i = 2; i <= FiboNumber; i++) { //Считаем этот член последовательности Фибоначчи
            Arr[i] = Arr[i - 1] + Arr[i - 2];
        }
        return Arr[FiboNumber] % m; // Фактически возвращаем остаток от деления F(n) на m
    }

    


}

