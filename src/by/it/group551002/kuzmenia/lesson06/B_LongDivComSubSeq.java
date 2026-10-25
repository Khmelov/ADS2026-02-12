package by.it.group551002.kuzmenia.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая кратная подпоследовательность

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] делится на предыдущий
    т.е. для всех 1<=j<k, A[i[j+1]] делится на A[i[j]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    4
    3 6 7 12

    Sample Output:
    3
*/

public class B_LongDivComSubSeq {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Массив для хранения длин подпоследовательностей
        int[] d = new int[n];
        int maxResult = 0;

        for (int i = 0; i < n; i++)
        {
            // Минимальная для любого элемента 1 (он сам)
            d[i] = 1;
            for (int j = 0; j < i; j++)
            {
                // Если текущий элемент делится на предыдущий, то обновляем
                if (m[i] % m[j] == 0)
                {
                    d[i] = Math.max(d[i], d[j] + 1);
                }
            }
            // Обновляем глобальный максимум
            maxResult = Math.max(maxResult, d[i]);
        }

        return maxResult;
    }

}