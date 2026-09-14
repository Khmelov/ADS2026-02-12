package by.it.group551003.sysoev.lesson06;

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

        // Алгоритм O(n log n) для наибольшей невозрастающей подпоследовательности
        // tail[l] = минимальный последний элемент невозрастающей подпоследовательности длины l+1
        int[] tail = new int[n];
        // lastIdx[l] = индекс элемента, который находится в tail[l] (для восстановления)
        int[] lastIdx = new int[n];
        // prev[i] = индекс предыдущего элемента в оптимальной подпоследовательности для m[i]
        int[] prev = new int[n];
        int length = 0; // текущая длина LNDS

        for (int i = 0; i < n; i++) {
            // бинарный поиск первой позиции, где tail[pos] < m[i]
            int left = 0;
            int right = length;
            while (left < right) {
                int mid = (left + right) / 2;
                if (tail[mid] < m[i]) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            int pos = left;

            // обновляем tail и lastIdx
            tail[pos] = m[i];
            lastIdx[pos] = i;

            // устанавливаем ссылку на предыдущий элемент
            if (pos > 0) {
                prev[i] = lastIdx[pos - 1];
            } else {
                prev[i] = -1;
            }

            if (pos == length) {
                length++;
            }
        }

        // восстановление индексов подпоследовательности
        int[] indices = new int[length];
        int cur = lastIdx[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            indices[i] = cur + 1; // переход к 1-индексации
            cur = prev[cur];
        }

        // вывод результата согласно условию
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(indices[i]);
        }
        System.out.println();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return length;
    }
}