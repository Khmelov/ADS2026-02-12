package by.it.group551002.kuzmenia.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая возрастающая подпоследовательность
см.     https://ru.wikipedia.org/wiki/Задача_поиска_наибольшей_увеличивающейся_подпоследовательности
        https://en.wikipedia.org/wiki/Longest_increasing_subsequence

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    где каждый элемент A[i[k]] больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]<A[i[j+1]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    1 3 3 2 6

    Sample Output:
    3
*/

public class A_LIS {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
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
            // Минимальная длина LIS для любого элемента 1 (он сам)
            d[i] = 1;
            for (int j = 0; j < i; j++)
            {
                // Если текущий элемент больше предыдущего, пробуем обновить d[i]
                if (m[j] < m[i])
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
