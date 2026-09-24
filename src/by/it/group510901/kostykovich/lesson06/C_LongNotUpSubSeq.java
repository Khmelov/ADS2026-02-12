package by.it.group510901.kostykovich.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
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

        int[] tails = new int[n + 1];
        int[] parent = new int[n];
        int length = 0;
        for (int i = 0; i < n; i++) {
            parent[i] = -1;

            int low = 1, high = length;
            while (low <= high) {
                int mid = (low + high) / 2;
                if (m[tails[mid]] >= m[i]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
            int newLen = low;

            if (newLen > 1) {
                parent[i] = tails[newLen - 1];
            }


            if (newLen > length || m[i] > m[tails[newLen]]) {
                tails[newLen] = i;
                if (newLen > length) {
                    length = newLen;
                }
            }
        }

        int[] indices = new int[length];
        int current = tails[length];
        for (int i = length - 1; i >= 0; i--) {
            indices[i] = current + 1;
            current = parent[current];
        }

        System.out.println(length);
        for (int i = 0; i < length; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(indices[i]);
        }
        System.out.println();

        result = length;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}