package by.it.group551002.yorsh.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        instance.getNotUpSeqSize(stream); // вывод происходит внутри метода
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

        // dp в виде оптимизированных массивов (метод patience sorting)
        // tailVal[k] – минимальный преобразованный элемент для подпоследовательности длины k+1
        int[] tailVal = new int[n];
        // tailIdx[k] – индекс исходного элемента, завершающего подпоследовательность из tailVal[k]
        int[] tailIdx = new int[n];
        // prev[i] – индекс предыдущего элемента для элемента i в его LIS
        int[] prev = new int[n];
        int size = 0;  // текущая длина наибольшей невозрастающей подпоследовательности

        for (int i = 0; i < n; i++) {
            // переворачиваем знак, чтобы свести задачу к поиску наибольшей нестрого возрастающей
            int x = -m[i];

            // бинарный поиск первого элемента в tailVal, который > x
            int pos = lowerBound(tailVal, size, x);

            tailVal[pos] = x;
            tailIdx[pos] = i;
            if (pos > 0) {
                prev[i] = tailIdx[pos - 1];
            } else {
                prev[i] = -1;
            }
            if (pos == size) {
                size++;
            }
        }

        // восстанавливаем саму подпоследовательность индексов
        List<Integer> indices = new ArrayList<>();
        for (int idx = tailIdx[size - 1]; idx != -1; idx = prev[idx]) {
            indices.add(idx + 1);   // перевод в индексацию с 1
        }
        Collections.reverse(indices);

        // вывод результата
        System.out.println(size);
        for (int i = 0; i < indices.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(indices.get(i));
        }
        System.out.println();

        int result = size;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;   // возвращаем длину (main её не печатает)
    }

    /** Бинарный поиск первого индекса в arr[0..len-1], где значение > x */
    private int lowerBound(int[] arr, int len, int x) {
        int lo = 0, hi = len;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (arr[mid] > x) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }
}