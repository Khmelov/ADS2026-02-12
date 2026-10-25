package by.it.group510901.filimonova.lesson01.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] arr = new int[n]; //массив для хранения последовательности
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt(); // заполняем массив числами из ввода
        }

        // Решение методом динамического программирования O(n^2)
        int result = lisDP(arr);

        // Альтернативное решение с бинарным поиском O(n log n) - более эффективное
        // int result = lisBinarySearch(arr);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // Решение 1: Динамическое программирование O(n^2)
    private int lisDP(int[] arr) {
        int n = arr.length;
        if (n == 0) return 0; //проверяем пумтой массив, если нет элементов, то длина 0

        // dp[i] - длина наибольшей возрастающей подпоследовательности,
        // заканчивающейся в элементе i
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // каждая подпоследовательность длины хотя бы 1

        int maxLength = 1; // переменная для отслеживания глобального максимума
        //начинаем с 1, тк хотя бы один элемент всегда есть

        for (int i = 1; i < n; i++) { //перебираем все эл-ты, начиная со 2 и ищем, куда его приклеить
            for (int j = 0; j < i; j++) { //перебираем все предыдущие эл-ты и можно ли продолжить
                if (arr[j] < arr[i]) { //предыдущий элемент должен быть строго меньше текущего
                    dp[i] = Math.max(dp[i], dp[j] + 1); //если условие выполнено, увеличиваем длину посл-ти
                }
            }
            maxLength = Math.max(maxLength, dp[i]); //после обработки, обновляем глобальный макс
        }

        return maxLength;
    }


    private int lisBinarySearch(int[] arr) {
        int n = arr.length; //проверка на пустой массив
        if (n == 0) return 0;

        // tails[i] - минимальный последний элемент возрастающей
        // подпоследовательности длины i+1
        int[] tails = new int[n];
        int length = 0; //текущ длина сам ой длинной посл-ти

        for (int num : arr) { //перебираем все
            int left = 0; //задаем границы и ищем позицию для вставки
            int right = length;

            // Бинарный поиск позиции для вставки num
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < num) { //если .. двигаем границу вправо
                    left = mid + 1;
                } else {
                    right = mid; //иначе двигаем правую границу к мид
                }
            }

            tails[left] = num; //вставляем в найденную позицию
            //если нашли элемент ≥ num, заменяем его на num (уменьшаем)
            //если все элементы < num, вставляем в конец
            if (left == length) {
                length++; //если вставили в конец массива, увеличиваем длину
            }
        }

        return length;
    }
}