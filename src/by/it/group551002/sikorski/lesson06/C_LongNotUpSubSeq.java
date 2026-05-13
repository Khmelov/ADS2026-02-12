package by.it.group551002.sikorski.lesson06;

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
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] a = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи методами динамического программирования (!!!)
        if (n == 0) return 0;

        // tailsIdx[i] хранит индекс последнего элемента подпоследовательности длины i+1
        int[] tailsIdx = new int[n];
        // prev[i] хранит индекс предыдущего элемента для i в подпоследовательности
        int[] prev = new int[n];
        int len = 0;

        for (int i = 0; i < n; i++) {
            // Бинарный поиск: ищем первый элемент в tailsIdx, который меньше a[i]
            // Т.к. ищем невозрастающую, tailsIdx будет содержать значения в невозрастающем порядке
            int l = 0, r = len - 1;
            int pos = len;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                if (a[tailsIdx[mid]] < a[i]) {
                    pos = mid;
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }

            if (pos == len) {
                // Увеличиваем длину подпоследовательности
                if (len > 0) prev[i] = tailsIdx[len - 1];
                else prev[i] = -1;
                tailsIdx[len++] = i;
            } else {
                // Обновляем существующую длину более выгодным (большим) значением
                if (pos > 0) prev[i] = tailsIdx[pos - 1];
                else prev[i] = -1;
                tailsIdx[pos] = i;
            }
        }

        // Восстановление пути (индексов)
        int[] resultIndices = new int[len];
        int curr = tailsIdx[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            resultIndices[i] = curr + 1; // Переводим в 1-индексацию
            curr = prev[curr];
        }

        // Вывод согласно Sample Output: сначала длина, потом индексы
        System.out.println(len);
        for (int i = 0; i < len; i++) {
            System.out.print(resultIndices[i] + (i == len - 1 ? "" : " "));
        }
        System.out.println();

        return len;
    }

}