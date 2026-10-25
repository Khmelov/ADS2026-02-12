package by.it.group551002.bochilo.lesson06;

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
        //LDS
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int[] prev = new int[n];
        Arrays.fill(prev, -1);
        for (int i = 1; i < n; i++) {
            for (int k = 0; k < i; k++) {
                if (m[i] <= m[k]) {
                    dp[i] = dp[k] + 1;
                    prev[i] = k;
                }
            }
        }
        //поиск в массиве dp максимального значения - result
        //и его индекса ind
        int result = 0, ind = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] > result) {
                result = dp[i];
                ind = i;
            }
        }
        //собираем последовательность индексов в обратном порядке
        int[] ind_seq = new int[result];
        int pos = result - 1;
        while (ind != -1) {
            ind_seq[pos--] = ind + 1;
            ind = prev[ind];
        }
        //выводим массив индексов
        for (int i = 0; i < ind_seq.length; i++) {
            System.out.print(ind_seq[i] + " ");
        }
        System.out.println();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}