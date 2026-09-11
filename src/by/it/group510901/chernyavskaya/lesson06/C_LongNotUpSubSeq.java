package by.it.group510901.chernyavskaya.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.ArrayList;

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
        // Исправлено: используем правильный класс для загрузки файла
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        // Метод getNotUpSeqSize уже выводит результат, так что здесь ничего не выводим
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // ========== АЛГОРИТМ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ ==========
        // Для n до 100000 используем алгоритм за O(n log n)

        // tails[i] - минимальный последний элемент НЕВОЗРАСТАЮЩЕЙ подпоследовательности длины i+1
        ArrayList<Integer> tails = new ArrayList<>();

        // tailsIndex[i] - индекс в исходном массиве элемента tails[i]
        ArrayList<Integer> tailsIndex = new ArrayList<>();

        // prev[i] - индекс предыдущего элемента в ННП для элемента i
        int[] prev = new int[n];

        // Основной цикл: обрабатываем каждый элемент массива
        for (int i = 0; i < n; i++) {
            // Бинарный поиск позиции для вставки m[i]
            // Ищем первую позицию pos, такую что tails[pos] < m[i]
            int left = 0;
            int right = tails.size();

            while (left < right) {
                int mid = (left + right) / 2;
                // Для невозрастающей последовательности (>=)
                // Если текущий элемент больше элемента в tails, идем влево
                if (tails.get(mid) < m[i]) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            int pos = left;

            // Сохраняем ссылку на предыдущий элемент
            if (pos > 0) {
                prev[i] = tailsIndex.get(pos - 1);
            } else {
                prev[i] = -1; // -1 означает, что это начало последовательности
            }

            // Вставляем или заменяем элемент
            if (pos == tails.size()) {
                tails.add(m[i]);
                tailsIndex.add(i);
            } else {
                tails.set(pos, m[i]);
                tailsIndex.set(pos, i);
            }
        }

        // Длина наибольшей невозрастающей подпоследовательности
        int length = tails.size();

        // Восстанавливаем индексы последовательности
        int[] indices = new int[length];
        int lastIndex = tailsIndex.get(length - 1);

        // Идем от последнего элемента к первому, используя prev
        for (int i = length - 1; i >= 0; i--) {
            indices[i] = lastIndex + 1; // +1, так как индексы в задаче начинаются с 1
            lastIndex = prev[lastIndex];
        }

        // Выводим результат
        System.out.println(length); // Первая строка: длина
        for (int i = 0; i < length; i++) {
            System.out.print(indices[i] + " "); // Вторая строка: индексы
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return length;
    }
}