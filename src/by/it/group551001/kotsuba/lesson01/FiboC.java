package by.it.group551001.kotsuba.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;

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
        long a=0,b=1,perlen=0;
        for(int i=0; i<6*m;i++){
            long temp = (a+b) % m;
            a=b;
            b= temp;
            if(a==0 && b==1){
                perlen= i+1;
                break;
            }
        }
        long newlen= n % perlen;
        if (newlen <= 1) return newlen;
        long num1=0,num2=1;
        for(int i=0;i<newlen-1;i++){
            long num3= (num1 + num2) % m;
            num1 = num2;
            num2 = num3;
        }
        return num2;
    }


}

