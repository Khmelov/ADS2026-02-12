package by.it.group551002.kuzmenia.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозростающая подпоследовательность

Дано:
    целое число 1<=n<=1E5 ( ОБРАТИТЕ ВНИМАНИЕ НА РАЗМЕРНОСТЬ! )
    массив A[1…n] натуральных чисел, не превосходящих 2E9.
s
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
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // tails[l] будет хранить значение последнего элемента невозрастающей подпоследовательности длины l + 1
        int[] tails = new int[n];
        // tailsIdx[l] хранит индекс этого элемента в исходном массиве a
        int[] tailsIdx = new int[n];
        // prev[i] хранит индекс предыдущего элемента для восстановления пути
        int[] prev = new int[n];

        int length = 0;

        for (int i = 0; i < n; i++)
        {
            // Ищем бинарным поиском место для a[i] в tails
            int lo = 0, hi = length;
            while (lo < hi)
            {
                int mid = (lo + hi) >>> 1;
                if (tails[mid] >= m[i])
                    // Ищем первое число, которое меньше a[i]
                    lo = mid + 1;
                else
                    hi = mid;
            }

            int pos = lo;
            tails[pos] = m[i];
            tailsIdx[pos] = i;

            // Если pos > 0, то предыдущий элемент в цепочке тот, который сейчас в tailsIdx[pos-1]
            prev[i] = (pos > 0) ? tailsIdx[pos - 1] : -1;

            if (pos == length)
                length++;
        }

        // Восстановление индексов
        int[] resultIndices = new int[length];
        int curr = tailsIdx[length - 1];
        for (int i = length - 1; i >= 0; i--)
        {
            resultIndices[i] = curr + 1;
            curr = prev[curr];
        }

        System.out.println(length);
        for (int i = 0; i < length; i++)
            System.out.print(resultIndices[i] + (i == length - 1 ? "" : " "));
        System.out.println();

        return length;
    }
}