package by.it.group510901.krot.lesson06;

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

        int n = scanner.nextInt();
        int[] m = new int[n];

        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // dp[i] — длина подпоследовательности, заканчивающейся на i
        int[] dp = new int[n];
        // prev[i] — индекс предыдущего элемента для восстановления пути
        int[] prev = new int[n];

        // tails[len] — индекс элемента, на котором заканчивается лучшая подпоследовательность длины len+1
        int[] tails = new int[n];

        int length = 0;  // текущая максимальная длина

        for (int i = 0; i < n; i++) {
            // бинарный поиск: ищем первую позицию, где элемент <= m[i]
            int left = 0;
            int right = length;

            while (left < right) {
                int mid = left + (right - left) / 2;
                if (m[tails[mid]] < m[i]) {
                    // если хвост mid слишком маленький, идём влево
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            // left — позиция, куда можно поставить текущий элемент
            dp[i] = left + 1;
            prev[i] = (left > 0) ? tails[left - 1] : -1;

            // обновляем tails
            tails[left] = i;

            if (left == length) {
                length++;
            }
        }

        // восстанавливаем индексы
        int[] indices = new int[length];
        int idx = tails[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            indices[i] = idx + 1;  // +1 потому что индексы с 1
            idx = prev[idx];
        }

        // выводим результат
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            System.out.print(indices[i]);
            if (i < length - 1) System.out.print(" ");
        }

        return length;
    }

}