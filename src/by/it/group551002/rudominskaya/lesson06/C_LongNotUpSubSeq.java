package by.it.group551002.rudominskaya.lesson06;

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
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        // читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Алгоритм: O(n log n) для невозрастающей подпоследовательности
        // Используем бинарный поиск для оптимизации

        // tails[j] - последний элемент невозрастающей подпоследовательности длины j+1
        // Для невозрастающей последовательности ищем первое значение, которое < текущего
        // (потому что нам нужно A[i] >= A[j] для j после i)
        int[] tails = new int[n];
        // pos[i] - позиция (индекс в исходном массиве) элемента, который стоит в tails на месте i
        int[] pos = new int[n];
        // prev[i] - индекс предыдущего элемента в ННП для элемента i
        int[] prev = new int[n];

        int length = 0; // текущая длина ННП

        for (int i = 0; i < n; i++) {
            int current = m[i];

            // Бинарный поиск: ищем первую позицию, где tails[pos] < current
            // (для невозрастающей: нужно, чтобы current <= предыдущего, т.е. tails[pos] < current нельзя ставить)
            // Используем вариант: ищем первую позицию, где tails[pos] < current
            int left = 0;
            int right = length;
            while (left < right) {
                int mid = (left + right) / 2;
                if (tails[mid] < current) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            // left - позиция, куда вставляем current
            tails[left] = current;
            pos[left] = i;

            // Запоминаем предыдущий элемент (для восстановления последовательности)
            if (left > 0) {
                prev[i] = pos[left - 1];
            } else {
                prev[i] = -1; // нет предыдущего
            }

            if (left == length) {
                length++;
            }
        }

        // Восстанавливаем индексы ННП (в порядке возрастания индексов)
        int[] indices = new int[length];
        int lastIndex = pos[length - 1];
        for (int j = length - 1; j >= 0; j--) {
            indices[j] = lastIndex + 1; // +1 потому что индексы в задаче начинаются с 1
            lastIndex = prev[lastIndex];
        }

        // Выводим результат
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(indices[i]);
        }
        System.out.println();

        int result = length;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}