package by.it.group551002.zolotenkov.lesson06;

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
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи методами динамического программирования (!!!)
        int result = 0;

        // d[len] – максимальный последний элемент невозрастающей подпоследовательности длины len
        int[] d = new int[n + 1];
        // pos[len] – индекс элемента в массиве m, который сейчас является хвостом для длины len
        int[] pos = new int[n + 1];
        // prev[i] – индекс предыдущего элемента для i-го элемента в найденной ННВП
        int[] prev = new int[n];
        // максимальная длина
        int maxLen = 0;
        d[0] = Integer.MAX_VALUE; // заглушка, чтобы любое число подходило для len=0

        for (int i = 0; i < n; i++) {
            int x = m[i];
            // бинарный поиск максимальной длины, для которой d[len] >= x
            int lo = 1, hi = maxLen;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                if (d[mid] >= x) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }
            // длина, которую можно продолжить
            int len = hi; // hi == максимальная длина с d[len] >= x
            int newLen = len + 1;
            // обновляем d[newLen] (максимизируем хвост)
            if (newLen > maxLen) {
                maxLen = newLen;
                d[newLen] = x;
                pos[newLen] = i;
            } else if (x > d[newLen]) {
                d[newLen] = x;
                pos[newLen] = i;
            }
            // запоминаем предшественника для элемента i
            prev[i] = (len == 0) ? -1 : pos[len];
        }

        // Восстановление индексов (индексы в ответе начинаются с 1)
        int[] indices = new int[maxLen];
        int cur = pos[maxLen];
        for (int k = maxLen - 1; k >= 0; k--) {
            indices[k] = cur + 1; // +1 для перехода к нумерации с 1
            cur = prev[cur];
        }

        // Вывод результата
        System.out.println(maxLen);
        for (int i = 0; i < maxLen; i++) {
            System.out.print(indices[i]);
            if (i < maxLen - 1) System.out.print(" ");
        }
        System.out.println(); // завершающий перевод строки

        result = maxLen;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}