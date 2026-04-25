package by.it.group551001.kuzminich.lesson6;

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
    // поиск: ищем самый левый элемент его индекс в массиве tail_index), который меньше нашего текущего числа
    int bin_search(int[] m, int[] tail_index, int size, int curr_value){
        int l = 0;
        int r = size - 1;
        int pos = size;

        while (l <= r) {
            int mid = (l + r) / 2;
            int middle_value = m[tail_index[mid]];
            if (middle_value < curr_value) {
                pos = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return pos;
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
        int[] prev = new int[n];
        int[] tail_index = new int[n];

        int result = 0;

        for (int i = 0; i<n; i++){ // индекс по массиву солдат m
            int pos = bin_search(m, tail_index, result, m[i]);
            // Если он встает НЕ на первый стол, его предок — тот, кто на столе слева
            if (pos > 0) {
                prev[i] = tail_index[result-1];
            } else{
                prev[i] =-1;
            }
            tail_index[pos] = i;
            if (pos == result){
                result++;
            }
        }
        int[] resultPath = new int[result];
        int curr = tail_index[result - 1];

        for (int i = result - 1; i >= 0; i--) {
            resultPath[i] = curr + 1; // +1, так как индексы в задаче с 1
            curr = prev[curr];        // Прыгаем к предку
        }

        // --- ВЫВОД ---
        System.out.println(result); // Первая строка: длина
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result; i++) {
            sb.append(resultPath[i]);
            if (i < result - 1) sb.append(" ");
        }
        System.out.println(sb.toString()); // Вторая строка: индексы через пробел


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

}