package by.it.group510901.dremluk.lesson06;

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
/*, суть задачи в том, что программа должна вывести максимальную длину массива*/
        int n = scanner.nextInt();   // длина массива
        int[] m = new int[n];        // сам массив
/* но теперь числа могут повторяться и отсортированы по убыванию*/
        // читаем массив
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // dp[i] — длина луучшей невозрастающей подпоследовательности, заканчивающейся в i
        int[] dp = new int[n];

        // prev[i] — предыдущий индекс в подпоследовательности
        int[] prev = new int[n];

        int result = 0;  // максимальная длина

        // перебираем каждый элемент как возможный конец подпоследовательности
        for (int i = 0; i < n; i++) {

            dp[i] = 1;      // минимальная длина — сам элемент
            prev[i] = -1;   // пока нет предыдущего

            // ищем подходящие элементы слева
            for (int j = 0; j < i; j++) {

                // условие невозрастания: m[j] >= m[i]
                // и проверяем, улучшает ли это длину dp[i]
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }

            // обновляем глобальный максимум
            if (dp[i] > result) {
                result = dp[i];
            }
        }

        return result;  // возвращаем длину подпоследовательности
    }

}