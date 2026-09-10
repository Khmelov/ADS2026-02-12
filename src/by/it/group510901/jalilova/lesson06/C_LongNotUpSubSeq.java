package by.it.group510901.jalilova.lesson06;

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
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        if (!scanner.hasNextInt()) return 0;
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // !!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        // Для поиска ННП (невозрастающей) a[i] >= a[j]
        // Это то же самое, что НВП (возрастающая) для инвертированных условий.

        int[] tails = new int[n]; // Храним значения элементов
        int[] tailsIndices = new int[n]; // Храним их индексы в исходном массиве
        int[] prev = new int[n]; // Для восстановления пути
        int len = 0;

        for (int i = 0; i < n; i++) {
            // Ищем бинарным поиском место для вставки a[i]
            // Так как последовательность НЕВОЗРАСТАЮЩАЯ, tails будет отсортирован по убыванию
            int low = 0, high = len;
            while (low < high) {
                int mid = (low + high) / 2;
                if (tails[mid] >= a[i]) { // Условие невозрастания
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }

            int pos = low;
            tails[pos] = a[i];
            tailsIndices[pos] = i;
            prev[i] = (pos > 0) ? tailsIndices[pos - 1] : -1;

            if (pos == len) {
                len++;
            }
        }

        // Восстановление индексов
        int[] resultIndices = new int[len];
        int curr = tailsIndices[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            resultIndices[i] = curr + 1; // +1 т.к. индексы в задаче с 1
            curr = prev[curr];
        }

        // Вывод индексов
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            System.out.print(resultIndices[i] + (i == len - 1 ? "" : " "));
        }
        System.out.println();

        int result = len;
        // !!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!

        return result;
    }
}