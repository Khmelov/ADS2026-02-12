package by.it.group551004.pedko.lesson06;

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

    int findMaxIndex(int[] data) {
        int max = 0;
        for (int i = 1; i < data.length; ++i) {
            if (data[i] > data[max])
                max = i;
        }
        return max;
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] ints = new int[n];
        int[] sizes = new int[n];
        int[] prevs = new int[n];

        ints[0] = scanner.nextInt();
        sizes[0] = 1;
        prevs[0] = 0;

        for(int i = 1; i < n; ++i) {
            ints[i] = scanner.nextInt();
            sizes[i] = 1;
            prevs[i] = i;
            for(int j = 0; j < i; ++j) {
                if(ints[i] <= ints[j] && sizes[i] <= sizes[j]) {
                    sizes[i] = sizes[j]+1;
                    prevs[i] = j;
                }
            }
        }

        StringBuilder sAnswer = new StringBuilder();

        int index = findMaxIndex(sizes);
        while(true) {
            sAnswer.insert(0, (index+1) + " ");
            if(prevs[index] == index)
                break;
            else
                index = prevs[index];
        }

        System.out.println(sAnswer);

        //тут реализуйте логику задачи методами динамического программирования (!!!)
        int answer = sizes[findMaxIndex(sizes)];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return answer;
    }
}