package by.it.group551003.delendik.lesson06;

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
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];

        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] P = new int[n];

        int[] M = new int[n + 1];

        int L = 0;

        for (int i = 0; i < n; i++) {

            int lo = 1;
            int hi = L;

            while (lo <= hi) {
                int mid = (lo + hi + 1) / 2;

                if (m[M[mid]] >= m[i]) {
                    lo = mid + 1;
                } else {
                    hi = mid - 1;
                }
            }

            int newL = lo;

            P[i] = (newL > 1) ? M[newL - 1] : -1;

            M[newL] = i;

            if (newL > L) {
                L = newL;
            }
        }

        int[] ans = new int[L];

        int cur = M[L];
        for (int i = L - 1; i >= 0; i--) {
            ans[i] = cur + 1;
            cur = P[cur];
        }

        System.out.println(L);
        for (int i = 0; i < L; i++) {
            System.out.print(ans[i] + " ");
        }

        return L;
    }


}