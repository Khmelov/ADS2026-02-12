package by.it.group510901.sidorov.lesson04;

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

    private int mergeAndCount(int[] a, int left, int mid, int right) {
        int[] L = new int[mid - left + 1];
        int[] R = new int[right - mid];

        for (int i = 0; i < L.length; i++) L[i] = a[left + i];
        for (int j = 0; j < R.length; j++) R[j] = a[mid + 1 + j];

        int i = 0, j = 0, k = left;
        int swaps = 0;

        while (i < L.length && j < R.length) {
            if (L[i] <= R[j]) {
                a[k++] = L[i++];
            } else {
                a[k++] = R[j++];
                // ГЛАВНЫЙ МОМЕНТ:
                // Если текущий элемент из R меньше элемента из L,
                // то он меньше ВСЕХ оставшихся элементов в L.
                swaps += (L.length - i);
            }
        }

        while (i < L.length) a[k++] = L[i++];
        while (j < R.length) a[k++] = R[j++];

        return swaps;
    }

    private int mergeSortAndCount(int[] a, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Считаем инверсии в левой и правой частях
            count += mergeSortAndCount(a, left, mid);
            count += mergeSortAndCount(a, mid + 1, right);

            // Считаем инверсии при слиянии этих частей
            count += mergeAndCount(a, left, mid, right);
        }
        return count;
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
        int result = mergeSortAndCount(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
