package by.it.group510902.yaskel.lesson06;

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
        //Общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи методами динамического программирования (!!!)
        int result = 0;

        if (n == 0) {
            System.out.println(0);
            System.out.println();
            return 0;
        }

        // tails[len] = индекс элемента в m, который даёт невозрастающую подпоследовательность длины len+1
        int[] tails = new int[n];
// prev[i] = индекс предыдущего элемента в подпоследовательности, заканчивающейся в i
        int[] prev = new int[n];

        int len = 0; // текущая длина найденной наибольшей невозрастающей подпоследовательности

        for (int i = 0; i < n; i++) {
            // бинарный поиск: ищем первую позицию в tails[0..len-1], где m[tails[pos]] < m[i]
            // для невозрастающей: нам нужно, чтобы m[tails[pos]] >= m[i] было как можно дальше
            int lo = -1;
            int hi = len;
            while (hi - lo > 1) {
                int mid = (lo + hi) >>> 1;
                if (m[tails[mid]] >= m[i]) {
                    lo = mid;
                } else {
                    hi = mid;
                }
            }
            int pos = hi; // позиция, куда вставляем текущий элемент

            prev[i] = (pos > 0) ? tails[pos - 1] : -1;

            if (pos == len) {
                tails[len++] = i;
            } else {
                tails[pos] = i;
            }
        }

// восстанавливаем подпоследовательность в обратном порядке
        int[] indices = new int[len];
        int cur = tails[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            indices[i] = cur + 1; // переводим в 1-based индекс
            cur = prev[cur];
        }

// выводим
        System.out.println(len);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i > 0) sb.append(' ');
            sb.append(indices[i]);
        }
        System.out.println(sb.toString());

        result = len;
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}