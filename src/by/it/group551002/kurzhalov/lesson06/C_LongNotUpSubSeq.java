package by.it.group551002.kurzhalov.lesson06;

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

        // Для решения за O(n log n) будем искать наибольшую невозрастающую подпоследовательность
        // Это эквивалентно поиску наибольшей возрастающей подпоследовательности
        // для обратных значений (инвертируем знак)

        // tails[i] - минимальное значение последнего элемента для подпоследовательности длины i+1
        int[] tails = new int[n];
        // pos[i] - позиция в tails, куда попал элемент m[i]
        int[] pos = new int[n];
        // prev[i] - индекс предыдущего элемента в подпоследовательности для восстановления
        int[] prev = new int[n];

        int len = 0; // текущая длина наибольшей подпоследовательности

        Arrays.fill(prev, -1);

        for (int i = 0; i < n; i++) {
            // Для невозрастающей подпоследовательности ищем первое значение <= m[i]
            // в массиве tails (который отсортирован по убыванию для невозрастающей)
            // Но проще инвертировать: ищем последнее значение >= m[i] в возрастающем tails

            // Бинарный поиск: ищем первую позицию, где tails[pos] < m[i]
            // (для невозрастающей подпоследовательности)
            int left = 0, right = len;
            while (left < right) {
                int mid = (left + right) / 2;
                if (tails[mid] >= m[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            int idx = left;
            tails[idx] = m[i];
            pos[idx] = i;

            if (idx > 0) {
                prev[i] = pos[idx - 1];
            }

            if (idx == len) {
                len++;
            }
        }

        // Восстановление последовательности индексов
        int[] indices = new int[len];
        int current = pos[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            indices[i] = current + 1; // +1 так как индексация с 1
            current = prev[current];
        }

        // Вывод результатов
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            System.out.print(indices[i]);
            if (i < len - 1) {
                System.out.print(" ");
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return len;
    }
}