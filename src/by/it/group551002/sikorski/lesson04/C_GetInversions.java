package by.it.group551002.sikorski.lesson04;

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
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        return (int) countInversions(a, 0, n - 1);
    }
    private long countInversions(int[] a, int left, int right) {
        long count = 0;
        if (left < right) {
            int mid = (left + right) / 2;

            // Считаем инверсии в левой и правой частях
            count += countInversions(a, left, mid);
            count += countInversions(a, mid + 1, right);

            // Считаем инверсии при слиянии
            count += mergeAndCount(a, left, mid, right);
        }
        return count;
    }

    private long mergeAndCount(int[] a, int left, int mid, int right) {
        int[] leftArray = java.util.Arrays.copyOfRange(a, left, mid + 1);
        int[] rightArray = java.util.Arrays.copyOfRange(a, mid + 1, right + 1);

        int i = 0, j = 0, k = left;
        long swaps = 0;

        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                a[k++] = leftArray[i++];
            } else {
                // Если элемент справа меньше, то он меньше всех оставшихся элементов слева
                a[k++] = rightArray[j++];
                swaps += (leftArray.length - i); // Вот здесь считаются инверсии
            }
        }

        while (i < leftArray.length) a[k++] = leftArray[i++];
        while (j < rightArray.length) a[k++] = rightArray[j++];

        return swaps;

    }
}
