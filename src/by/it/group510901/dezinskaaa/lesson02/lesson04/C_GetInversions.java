package by.it.group510901.dezinskaaa.lesson02.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
рассчитать число инверсий одномерного массива.
сложность алгоритма должна быть не хуже, чем O(n log n)

первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (такая пара элементов называется инверсией массива.
    количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

sample input:
5
2 3 9 2 9
sample output:
2

головоломка (т.е. не обязательно).
попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
докажите рост производительности замерами времени.
большой тестовый массив можно прочитать свой или сгенерировать его программно.
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

    // метод слияния, который также подсчитывает инверсии
    private int mergeAndCount(int[] arr, int left, int mid, int right) {
        // создаём временные массивы
        int[] leftArr = new int[mid - left + 1];
        int[] rightArr = new int[right - mid];

        for (int i = 0; i < leftArr.length; i++)
            leftArr[i] = arr[left + i];
        for (int j = 0; j < rightArr.length; j++)
            rightArr[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        int inversions = 0;

        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                // все оставшиеся элементы в leftArr больше текущего rightArr[j]
                inversions += (leftArr.length - i);
                j++;
            }
            k++;
        }

        // копируем остатки
        while (i < leftArr.length) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }
        while (j < rightArr.length) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
        return inversions;
    }

    // рекурсивная сортировка слиянием с подсчётом инверсий
    private int mergeSortAndCount(int[] arr, int left, int right) {
        int inversions = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            inversions += mergeSortAndCount(arr, left, mid);
            inversions += mergeSortAndCount(arr, mid + 1, right);
            inversions += mergeAndCount(arr, left, mid, right);
        }
        return inversions;
    }

    int calc(InputStream stream) throws FileNotFoundException {
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     начало задачи     !!!!!!!!!!!!!!!!!!!!!!!!
        // размер массива
        int n = scanner.nextInt();
        // сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!

        if (n > 0) {
            result = mergeSortAndCount(a, 0, n - 1);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     конец задачи     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}