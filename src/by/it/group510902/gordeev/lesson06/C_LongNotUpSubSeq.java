package by.it.group510902.gordeev.lesson06;

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

        if (n == 0) {
            System.out.println(0);
            return 0;
        }

        // dp[i] = длина LNIS, заканчивающейся в позиции i (для восстановления)
        int[] dp = new int[n];
        int[] parent = new int[n]; // родитель в подпоследовательности
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            parent[i] = -1;
        }

        // tails[len] = максимальное значение, которым может заканчиваться
        // невозрастающая подпоследовательность длины len+1
        int[] tails = new int[n];
        int[] tailIndex = new int[n]; // индекс элемента для tails[len]
        int length = 0; // текущая максимальная длина
        int bestEnd = 0; // индекс последнего элемента оптимальной подпоследовательности

        for (int i = 0; i < n; i++) {
            // Бинарный поиск: ищем левую позицию, где tails[pos] < m[i]
            int left = 0, right = length;
            while (left < right) {
                int mid = (left + right) / 2;
                if (tails[mid] < m[i]) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            int pos = left;

            // Обновляем tails и восстанавливаем связь
            tails[pos] = m[i];
            tailIndex[pos] = i;

            if (pos > 0) {
                parent[i] = tailIndex[pos - 1];
                dp[i] = dp[parent[i]] + 1;
            }

            if (pos == length) {
                length++;
                bestEnd = i;
            } else if (dp[i] > dp[bestEnd]) {
                bestEnd = i;
            }
        }

        // Восстанавливаем индексы (в обратном порядке)
        int[] indices = new int[length];
        int cur = bestEnd;
        for (int i = length - 1; i >= 0; i--) {
            indices[i] = cur + 1; // перевод в 1-индексацию
            cur = parent[cur];
        }

        // Вывод результата
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(indices[i]);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return length;
    }

}