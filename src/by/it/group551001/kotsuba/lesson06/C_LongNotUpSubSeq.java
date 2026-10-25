package by.it.group551001.kotsuba.lesson06;

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
        int[] tailsIndices = new int[n];
        int[] prev = new int[n];
        int result = 0;

        for (int i = 0; i < n; i++) {
            int val = m[i];

            int low = 0;
            int high = result;
            while (low < high) {
                int mid = (low + high) / 2;
                if (tails[mid] >= val) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }

            tails[low] = val;
            tailsIndices[low] = i;

            if (low > 0) {
                prev[i] = tailsIndices[low - 1];
            } else {
                prev[i] = -1;
            }

            if (low == result) {
                result++;
            }
        }

        int[] resultIndices = new int[result];
        int curr = tailsIndices[result - 1];
        for (int i = result - 1; i >= 0; i--) {
            resultIndices[i] = curr + 1;
            curr = prev[curr];
        }

        System.out.println(result);
        for (int i = 0; i < result; i++) {
            System.out.print(resultIndices[i] + (i == result - 1 ? "" : " "));
        }
        System.out.println();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}