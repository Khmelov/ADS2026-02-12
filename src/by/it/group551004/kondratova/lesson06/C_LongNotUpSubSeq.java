package by.it.group551004.kondratova.lesson06;

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
 C_LongNotUpSubSeq — Наибольшая невозрастающая подпоследовательность
Чтение данных — считывается размер массива и сам массив чисел.
Инициализация — создаются два массива: dp (длины подпоследовательностей) и prev (индексы предыдущих элементов). dp заполняется единицами, prev — значениями -1 (означает, что предыдущего элемента нет).
Внешний цикл (по i от 1 до n-1) — текущий элемент как конец подпоследовательности.
Внутренний цикл (по j от 0 до i-1) — перебирает все предыдущие элементы.
Проверка условия — если m[j] >= m[i] (элемент не больше предыдущего) и dp[j] + 1 > dp[i] (новая последовательность длиннее текущей), то условие выполняется.
Обновление dp[i] и prev[i] — dp[i] = dp[j] + 1, prev[i] = j (запоминаем индекс предыдущего элемента для восстановления пути).
Отслеживание максимума — запоминаются максимальная длина и индекс последнего элемента этой подпоследовательности.
Восстановление индексов — начиная с последнего элемента, по цепочке prev восстанавливаются все индексы подпоследовательности в обратном порядке.
Корректировка индексов — к каждому индексу прибавляется 1, так как в условии задачи нумерация элементов начинается с 1, а в массиве — с 0.
Вывод результата — сначала выводится длина, затем индексы элементов в порядке возрастания.

*/

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int[] result = instance.getNotUpSeqSize(stream);  // ← исправлено: int[] result
        System.out.println(result[0]);
        for (int i = 1; i <= result[0]; i++) {
            System.out.print(result[i] + " ");
        }
    }

    int[] getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // dp[i] - длина наибольшей невозрастающей подпоследовательности,
        // заканчивающейся на m[i]
        int[] dp = new int[n];
        // prev[i] - индекс предыдущего элемента в подпоследовательности
        int[] prev = new int[n];

        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            prev[i] = -1;
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
        }

        // Находим максимальную длину и последний элемент
        int maxLength = 0;
        int lastIndex = -1;
        for (int i = 0; i < n; i++) {
            if (dp[i] > maxLength) {
                maxLength = dp[i];
                lastIndex = i;
            }
        }

        // Восстанавливаем индексы
        int[] result = new int[maxLength + 1];
        result[0] = maxLength;
        int index = maxLength;
        int current = lastIndex;
        while (current != -1) {
            result[index] = current + 1; // +1 потому что индексы в задаче с 1
            index--;
            current = prev[current];
        }

        return result;
    }
}