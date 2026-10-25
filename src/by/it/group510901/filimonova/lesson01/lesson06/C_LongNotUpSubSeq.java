package by.it.group510901.filimonova.lesson01.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] arr = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        // Решение с использованием бинарного поиска O(n log n)
        List<Integer> resultIndices = longestNonIncreasingSubsequence(arr);

        // Выводим результат
        System.out.println(resultIndices.size());
        for (int i = 0; i < resultIndices.size(); i++) {
            System.out.print(resultIndices.get(i) + " ");
        }
        System.out.println();

        int result = resultIndices.size();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    private List<Integer> longestNonIncreasingSubsequence(int[] arr) {
        int n = arr.length;

        // tails[i] - последний элемент невозрастающей подпоследовательности длины i+1
        // храним пары (значение, индекс)
        int[] tails = new int[n];
        int[] tailsIndex = new int[n]; // индекс элемента в исходном массиве
        int[] prevIndex = new int[n];  // для восстановления последовательности
        Arrays.fill(prevIndex, -1);

        int length = 0;

        for (int i = 0; i < n; i++) {
            // Бинарный поиск позиции для вставки
            // Для невозрастающей последовательности ищем место, где tails[pos] < arr[i]
            // (так как нам нужно arr[i] <= предыдущего)
            int left = 0;
            int right = length;

            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] >= arr[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // Если нашли место для вставки
            int pos = left;
            tails[pos] = arr[i];
            tailsIndex[pos] = i;

            // Запоминаем предыдущий элемент для восстановления
            if (pos > 0) {
                prevIndex[i] = tailsIndex[pos - 1];
            }

            if (pos == length) {
                length++;
            }
        }

        // Восстанавливаем последовательность индексов
        List<Integer> result = new ArrayList<>();
        if (length > 0) {
            int currentIndex = tailsIndex[length - 1];
            while (currentIndex != -1) {
                result.add(0, currentIndex + 1); // +1 для перехода к 1-базису
                currentIndex = prevIndex[currentIndex];
            }
        }

        return result;
    }
}