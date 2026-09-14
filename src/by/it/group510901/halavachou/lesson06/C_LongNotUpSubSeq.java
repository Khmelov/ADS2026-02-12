package by.it.group510901.halavachou.lesson06;

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
        // Здесь выводим только длину, но сама задача требует и длину, и индексы
        // Поэтому метод getNotUpSeqSize должен выводить всё или возвращать длину после вывода
        // Оставим вывод длины здесь, а внутри метод сам выведет индексы
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
        //тут реализуйте логику задачи методами динамического программирования (!!!)

        // Массив для хранения "хвостов" подпоследовательностей разной длины
        // tail[i] - последний элемент наилучшей невозрастающей подпоследовательности длины i+1
        int[] tail = new int[n];
        // Массив для хранения индексов элементов в tail
        int[] tailIndex = new int[n];
        // Массив для восстановления пути: prev[i] - индекс предыдущего элемента в подпоследовательности
        int[] prev = new int[n];

        int length = 0; // текущая максимальная длина

        for (int i = 0; i < n; i++) {
            // Для невозрастающей последовательности ищем первое число в tail, которое МЕНЬШЕ m[i]
            // (чтобы сохранить невозрастание)
            int left = 0;
            int right = length;

            while (left < right) {
                int mid = (left + right) / 2;
                // tail отсортирован по убыванию для невозрастающей последовательности
                if (tail[mid] >= m[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // left - позиция, куда можно поставить текущий элемент
            tail[left] = m[i];
            tailIndex[left] = i;

            // Запоминаем предыдущий элемент
            if (left > 0) {
                prev[i] = tailIndex[left - 1];
            } else {
                prev[i] = -1;
            }

            if (left == length) {
                length++;
            }
        }

        int result = length;

        // Восстановление индексов
        int[] indices = new int[length];
        int currentIndex = tailIndex[length - 1];

        for (int i = length - 1; i >= 0; i--) {
            indices[i] = currentIndex + 1; // +1 так как индексация с 1
            currentIndex = prev[currentIndex];
        }

        // Вывод результата
        System.out.println(result);
        for (int i = 0; i < length; i++) {
            System.out.print(indices[i] + " ");
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}