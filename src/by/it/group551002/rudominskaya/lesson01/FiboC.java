package by.it.group551002.rudominskaya.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на mg
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
        if (n <= 1) {
            return n;
        }
        long prev = 0; // предыдущее число Фибоначчи
        long curr = 1; // текущее число
        long plength = 0; //длина периода Пизано

        //поиск длины периода
        for (int i = 0; i < m * 6; i++) {
            long temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
            if (prev == 0 && curr == 1) {
                plength = i + 1;
                break;
            }
        }

        //по свойству периода Пизано: F(n) % m = F(n % plength) % m,
        // где plength - длина периода Пизано для модуля m
        n = n % plength; //можем ускорить алгоритм
                         //остаток от 55 равен остатку от 55555 при делении на 1500

        prev = 0;
        curr = 1;
        for (int i = 2; i <= n; i++) {
            long temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
        }
        return (n == 0) ? prev : curr;
    }
}




