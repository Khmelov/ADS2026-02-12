package by.it.group510902.Tarnogurskaya.lesson06;

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
    для которой каждый элемент A[i[k]]не больше любого предыдущего
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
        instance.getNotUpSeqSize(stream);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] d = new int[n + 2]; // d[len] = последний элемент ПВП длины len
        int[] p = new int[n + 2]; // p[len] = индекс этого элемента
        int[] prev = new int[n]; // prev[i] = предыдущий индекс в ПВП для i

        for (int i = 0; i <= n + 1; i++) {
            d[i] = -1; // -1 означает "пусто"
            p[i] = -1;
        }

        d[0] = Integer.MAX_VALUE;
        int res = 0;  // текущая максимальная длина

        for (int i = 0; i < n; i++) {
            // Бинарный поиск: находим самую длинную подпоследовательность, у которой последний элемент >= m[i]
            int left = 0, right = n + 1, j = 0;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (d[mid] >= m[i]) {
                    j = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            // Обновляем массивы
            if (d[j] >= m[i] && (d[j + 1] < m[i] || d[j + 1] == -1)) {
                d[j + 1] = m[i];
                prev[i] = p[j];
                p[j + 1] = i;
                if (j + 1 > res) {
                    res = j + 1;
                }
            }
        }

        // Восстановление индексов
        int[] indices = new int[res];
        int current = p[res];
        for (int i = res - 1; i >= 0; i--) {
            indices[i] = current + 1;
            if (current >= 0) {
                current = prev[current];
            }
        }

        System.out.println(res);
        for (int i = 0; i < res; i++) {
            System.out.print(indices[i] + " ");
        }
        System.out.println();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return res;
    }
}