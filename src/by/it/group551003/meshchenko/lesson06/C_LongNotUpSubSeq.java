// C_LongNotUpSubSeq.java
package by.it.group551003.meshchenko.lesson06;

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
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Задача сводится к поиску длины наибольшей невозрастающей подпоследовательности.
        // Инвертируем знак: пусть b[i] = -m[i]. Тогда невозрастание m[i] >= m[i+1] равносильно
        // неубыванию b[i] <= b[i+1]. Решаем поиском наибольшей неубывающей подпоследовательности (LNDS).
        int[] tails = new int[n];
        int len = 0;
        for (int i = 0; i < n; i++) {
            int val = -m[i];
            // Поиск первого элемента, большего val (для неубывающей последовательности)
            int pos = upperBound(tails, len, val);
            tails[pos] = val;
            if (pos == len) {
                len++;
            }
        }
        return len;
    }

    // Бинарный поиск первого индекса в массиве arr[0..len-1], где arr[idx] > key.
    private int upperBound(int[] arr, int len, int key) {
        int lo = 0, hi = len; // hi == len означает, что потенциально можно поставить в конец
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] <= key) {
                lo = mid + 1;
            }
            else {
                hi = mid;
            }
        }
        return lo;
    }
}