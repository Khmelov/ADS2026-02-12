package by.it.group551003.kalach.lesson04;

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
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Вызываем рекурсивную функцию подсчета
        return countInversions(a, 0, n - 1);
    }

    private int countInversions(int[] a, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Считаем инверсии в левой и правой частях
            count += countInversions(a, left, mid);
            count += countInversions(a, mid + 1, right);

            // Считаем инверсии при слиянии
            count += mergeAndCount(a, left, mid, right);
        }
        return count;
    }

    private int mergeAndCount(int[] a, int left, int mid, int right) {
        int[] leftArr = new int[mid - left + 1];
        int[] rightArr = new int[right - mid];

        // Копируем данные во временные массивы
        System.arraycopy(a, left, leftArr, 0, leftArr.length);
        System.arraycopy(a, mid + 1, rightArr, 0, rightArr.length);

        int i = 0, j = 0, k = left;
        int inversions = 0;

        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                a[k++] = leftArr[i++];
            } else {
                // Если элемент справа меньше, чем слева — это инверсия
                a[k++] = rightArr[j++];
                // Все элементы в leftArr от текущего i до конца больше, чем rightArr[j]
                inversions += (leftArr.length - i);
            }
        }

        // Копируем оставшиеся элементы
        while (i < leftArr.length) a[k++] = leftArr[i++];
        while (j < rightArr.length) a[k++] = rightArr[j++];

        return inversions;
    }
}
