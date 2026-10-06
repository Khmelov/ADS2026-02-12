package by.it.group551002.puteev.lesson06;

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
        int[] tails = new int[n];
        int[] tailsInd = new int[n]; // Индексы элементов в tails
        int[] prev = new int[n];    // Для восстановления пути
        int length = 0;

        for (int i = 0; i < n; i++) {
            // Ищем бинарным поиском место для m[i]
            // В невозрастающей последовательности tails будет отсортирован по убыванию
            int left = 0;
            int right = length;
            while (left < right) {
                int mid = (left + right) / 2;
                if (tails[mid] >= m[i]) { // Условие невозрастания
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // Обновляем tails
            tails[left] = m[i];
            tailsInd[left] = i;

            // Запоминаем предка для восстановления пути
            if (left > 0) {
                prev[i] = tailsInd[left - 1];
            } else {
                prev[i] = -1;
            }

            if (left == length) {
                length++;
            }
        }

        // Восстановление индексов
        int[] resultIndices = new int[length];
        int curr = tailsInd[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            resultIndices[i] = curr + 1; // +1 т.к. индексы в задаче с 1
            curr = prev[curr];
        }

        // Вывод по формату задачи
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            System.out.print(resultIndices[i] + (i == length - 1 ? "" : " "));
        }
        System.out.println();

        return length;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

}