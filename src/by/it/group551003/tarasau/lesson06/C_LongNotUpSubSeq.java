package by.it.group551003.tarasau.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        int result = 1;

        int[] tails = new int[n];
        int[] tailsI = new int[n];
        int[] prev = new int[n];
        int length = 0;

        for (int i = 0; i < n; i++) {
            int left = 0, right = length;
            // so curr length is 0, we just put the first elem in
            // then we search in range from 0 to 1, and if elem is >=
            // we limit it to mid + 1 from left, which puts it through (left == right)
            // adding it to the LIS, iterating, range expanding, the binary search here
            // technically operates based off of previous info (last LIS elem), expands length/arr if the elem
            // is bigger than all of the previous ones using the info that elems before the last one
            // are all ordered, and changes the already known ones
            // length never decreases this way, though we only get one of the possible sequences this way
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] >= m[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            tails[left] = m[i];
            tailsI[left] = i;

            // previous elem in LIS for m[i] — tailsI[left-1]
            if (left > 0) {
                prev[i] = tailsI[left - 1];
            } else {
                prev[i] = -1;
            }

            if (left == length) {
                length++;
            }
        }

        List<Integer> seq = new ArrayList<>();
        int current = tailsI[length - 1];
        while (current != -1) {
            seq.add(current + 1);
            current = prev[current];
        }

        seq = seq.reversed();

        System.out.println("Длина: " + length);
        System.out.println("Индексы: " + seq);

        result = length;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}