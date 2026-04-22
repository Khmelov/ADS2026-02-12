package by.it.group551001.evtushenko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозростающая подпоследовательность

Дано:
    целое число 1<=n<=1E5 ( ОБРАТИТЕ ВНИМАНИЕ НА РАЗМЕРНОСТЬ! )
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] не больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]>=A[i[j+1]].

    В первой строке выведите её длину k,
    во второй - её индексы i[1]<i[2]<…<i[k]
    соблюдая A[i[1]]>=A[i[2]]>= ... >=A[i[n]].

    (индекс начинается с 1)

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    5 3 4 4 2

    Sample Output:
    4
    1 3 4 5
*/


public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи методами динамического программирования (!!!)
        int result = 0;

        for (int i = 0; i < m.length / 2; i++) {
            int t = m[i];
            m[i] = m[m.length - 1 - i];
            m[m.length - 1 - i] = t;
        }


    
        int[] d = new int[n + 1];
        d[0] = Integer.MIN_VALUE;
        for(int i = 1; i <= n; ++i) d[i] = Integer.MAX_VALUE;

        for(int i = 0; i < n; ++i){
            int l = upperBound(d, 0, n, m[i]);

            if (d[l-1] <= m[i] && m[i] <= d[l])
                d[l] = m[i];
        }

        for(int l = 0; l <= n; ++l)
            if(d[l] < Integer.MAX_VALUE)
                result = l;



        for (int i = 0; i < m.length / 2; i++) {
            int t = m[i];
            m[i] = m[m.length - 1 - i];
            m[m.length - 1 - i] = t;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    int upperBound(int a[], int l, int r, int v){
        while(l < r){
            int m = (l + r) / 2;
            if(v >= a[m])
                l = m + 1;
            else r = m;
        }

        return l;
    }
}