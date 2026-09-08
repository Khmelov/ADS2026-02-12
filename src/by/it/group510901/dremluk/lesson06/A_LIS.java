package by.it.group510901.dremluk.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая возрастающая подпоследовательность
см.     https://ru.wikipedia.org/wiki/Задача_поиска_наибольшей_увеличивающейся_подпоследовательности
        https://en.wikipedia.org/wiki/Longest_increasing_subsequence

Дано:
    целое число 1≤n≤1000
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    где каждый элемент A[i[k]] больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]<A[i[j+1]].

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    1 3 3 2 6

    Sample Output:
    3
*/

public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        // нам дан сразу отсортированный массиив чисел, суть
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        // задачи в том, что программа должна вывести максимальную
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        // длину массива, у которого числа идут по возрастанию, но
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //  менять местами числа в первоначальном массиве нельзя
        int n = scanner.nextInt();
        int[] m = new int[n]; // массив для хранения исходной последовательности

        // 2. Заполняем массив числами из входного потока
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // 3. Инициализация динамического программирования
        int result = 0;
        int[] dp = new int[n];
        // dp[i] — это длина НАИБОЛЬШЕЙ возрастающей подпоследовательности,
        // которая заканчивается элементом m[i].

        // 4. Основной цикл: заполняем dp[i] для всех i
        for (int i = 0; i < n; i++) {
            // Каждый элемент сам по себе — подпоследовательность длины 1
            dp[i] = 1;

            // Просматриваем все предыдущие элементы j (строго до i)
            for (int j = 0; j < i; j++) {
                // Если элемент m[j] МЕНЬШЕ, чем m[i],
                // то мы можем продлить подпоследовательность,
                // заканчивающуюся на m[j], добавляя m[i] в конец.
                if (m[j] < m[i]) {
                    // Новая длина = dp[j] + 1 (удлиняем цепочку на m[i])
                    // Берём максимум между текущим dp[i] и новым вариантом
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }

            // Обновляем глобальный максимум (длину НВП во всей последовательности)
            result = Math.max(result, dp[i]);
        }

        // Пример: для последовательности [1, 3, 3, 2, 6]
        // dp = [1, 2, 1, 2, 3]
        // result = 3

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}