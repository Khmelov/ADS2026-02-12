package by.it.group551003.kalach.lesson06;

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
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // tails[i] будет хранить индекс элемента, на который заканчивается
        // подпоследовательность длиной i + 1.
        // При этом мы стараемся, чтобы этот элемент был как можно больше.
        int[] tails = new int[n];
        // prev[i] хранит индекс предыдущего элемента для восстановления пути
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            prev[i] = -1;
        }

        int length = 0;

        for (int i = 0; i < n; i++) {
            // Ищем бинарным поиском место для m[i] в массиве tails.
            // Нам нужна "невозрастающая", поэтому ищем первый элемент, который меньше m[i].
            int low = 0;
            int high = length - 1;
            int pos = length;

            while (low <= high) {
                int mid = (low + high) / 2;
                if (m[tails[mid]] >= m[i]) {
                    low = mid + 1;
                } else {
                    pos = mid;
                    high = mid - 1;
                }
            }

            if (pos > 0) {
                prev[i] = tails[pos - 1];
            }

            tails[pos] = i;

            if (pos == length) {
                length++;
            }
        }

        // Восстановление пути
        int[] resultIndices = new int[length];
        int curr = tails[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            resultIndices[i] = curr + 1; // +1 т.к. индексы в задаче с 1
            curr = prev[curr];
        }

        // Вывод по формату (длина уже в переменной length)
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            System.out.print(resultIndices[i] + (i == length - 1 ? "" : " "));
        }
        System.out.println();

        int result = length;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}