package by.it.group510902.vinnichuk.lesson06;

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
        // массивы для хранения динамики и восстановления пути
        int[] tails = new int[n];
        int[] tailIdx = new int[n];
        int[] prev = new int[n];

        int len = 0;

        // ищу подпоследовательность за n log n
        for (int i = 0; i < n; i++) {
            int x = m[i];

            // бинарный поиск в убывающем массиве
            int l = 0;
            int r = len;
            while (l < r) {
                int mid = l + (r - l) / 2;
                if (tails[mid] >= x) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }

            tails[l] = x;
            tailIdx[l] = i;

            if (l > 0) {
                prev[i] = tailIdx[l - 1];
            } else {
                prev[i] = -1;
            }

            if (l == len) {
                len++;
            }
        }

        // восстанавливаю индексы
        int[] indices = new int[len];
        int curr = tailIdx[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            indices[i] = curr + 1;
            curr = prev[curr];
        }

        // вывожу индексы
        for (int i = 0; i < len; i++) {
            System.out.print(indices[i] + (i == len - 1 ? "" : " "));
        }
        System.out.println();

        int result = len;


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}