package by.it.group510902.kozak.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

Sample Input:
5
2 3 9 2 9
Sample Output:
2

Головоломка (т.е. не обязательно).
Попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
Докажите рост производительности замерами времени.
Большой тестовый массив можно прочитать свой или сгенерировать его программно.
*/


public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = (int) mergeSort(a, 0, a.length - 1);;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!


        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    private long mergeSort(int[] a, int left, int right) {
        if (left >= right) return 0; // 1 элемент — инверсий нет

        long inversions = 0;
        int mid = left + (right - left) / 2;

        inversions += mergeSort(a, left, mid);       // считаем в левой половине
        inversions += mergeSort(a, mid + 1, right);  // считаем в правой половине
        inversions += merge(a, left, mid, right);    // считаем при слиянии

        return inversions;
    }

    private long merge(int[] a, int left, int mid, int right) {
        long inversions = 0;

        int leftSize  = mid - left + 1;
        int rightSize = right - mid;

        int[] L = new int[leftSize];
        int[] R = new int[rightSize];

        for (int i = 0; i < leftSize; i++)  L[i] = a[left + i];
        for (int j = 0; j < rightSize; j++) R[j] = a[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < leftSize && j < rightSize) {
            if (L[i] <= R[j]) {
                a[k++] = L[i++];
            } else {
                // L[i] > R[j] — все оставшиеся элементы L образуют инверсию с R[j]
                inversions += (leftSize - i);
                a[k++] = R[j++];
            }
        }

        while (i < leftSize)  a[k++] = L[i++];
        while (j < rightSize) a[k++] = R[j++];

        return inversions;
    }
}
