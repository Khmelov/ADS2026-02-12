package by.it.group551003.kardash.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

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
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // tails[len] = минимальный возможный последний элемент
        // невозрастающей подпоследовательности длины len+1
        // (храним индекс, а не значение, для восстановления)
        int[] tails = new int[n];
        // prev[i] = индекс предыдущего элемента в подпоследовательности,
        // заканчивающейся в i
        int[] prev = new int[n];
        // pos[len] = индекс в массиве m последнего элемента подпоследовательности длины len+1
        int[] pos = new int[n];

        Arrays.fill(prev, -1);

        int length = 0; // текущая максимальная длина

        for (int i = 0; i < n; i++) {
            // Бинарный поиск: ищем первый tails[j] < m[i]
            // (т.к. нам нужна невозрастающая последовательность,
            //  ищем позицию для вставки слева)
            int left = 0, right = length;
            while (left < right) {
                int mid = (left + right) / 2;
                // tails хранит индексы, сравниваем значения
                // для невозрастающей: m[pos[mid]] >= m[i] - подходит
                // ищем первый, где m[pos[mid]] < m[i]
                if (m[pos[mid]] >= m[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // left - позиция, куда вставляем
            int j = left;
            tails[j] = i;
            pos[j] = i;
            if (j > 0) {
                prev[i] = pos[j - 1];
            }

            if (j == length) {
                length++;
            }
        }

        // Восстановление ответа
        int[] result = new int[length];
        int k = pos[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            result[i] = k + 1; // +1 т.к. индексы с 1
            k = prev[k];
        }

        // Вывод
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            System.out.print(result[i]);
            if (i < length - 1) System.out.print(" ");
        }
        System.out.println();

        return length;
    }

}