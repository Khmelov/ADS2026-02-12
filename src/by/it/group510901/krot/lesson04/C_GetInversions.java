package by.it.group510901.krot.lesson04;

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
        Scanner scanner = new Scanner(stream);

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int result = mergeSortAndCount(a, 0, n - 1);
        return result;
    }

    // сортировка слиянием + подсчёт инверсий
    int mergeSortAndCount(int[] array, int left, int right) {
        int count = 0;

        if (left < right) {
            int middle = (left + right) / 2;

            // считаем инверсии в левой и правой частях
            count += mergeSortAndCount(array, left, middle);
            count += mergeSortAndCount(array, middle + 1, right);

            // считаем инверсии при слиянии
            count += mergeAndCount(array, left, middle, right);
        }

        return count;
    }

    // слияние с подсчётом инверсий
    int mergeAndCount(int[] array, int left, int middle, int right) {
        int sizeLeft = middle - left + 1;
        int sizeRight = right - middle;

        int[] leftArray = new int[sizeLeft];
        int[] rightArray = new int[sizeRight];

        for (int i = 0; i < sizeLeft; i++) {
            leftArray[i] = array[left + i];
        }
        for (int i = 0; i < sizeRight; i++) {
            rightArray[i] = array[middle + 1 + i];
        }

        int i = 0, j = 0, k = left;
        int inversions = 0;

        while (i < sizeLeft && j < sizeRight) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
                //все оставшиеся элементы в левой части > rightArray[j]
                inversions += (sizeLeft - i);
            }
            k++;
        }

        while (i < sizeLeft) {
            array[k] = leftArray[i];
            i++;
            k++;
        }
        while (j < sizeRight) {
            array[k] = rightArray[j];
            j++;
            k++;
        }

        return inversions;
    }
}
