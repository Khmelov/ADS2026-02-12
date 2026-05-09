package by.it.group510902.slesar.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием

        a = mergeSort(a);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    int[] mergeSort(int[] arr) {
        // если 1 элемент — уже отсортирован
        if (arr.length <= 1) {
            return arr;
        }

        int mid = arr.length / 2;

        // делим массив на 2 части
        int[] left = new int[mid];
        int[] right = new int[arr.length - mid];

        // копируем левую часть
        for (int i = 0; i < mid; i++) {
            left[i] = arr[i];
        }

        // копируем правую часть
        for (int i = mid; i < arr.length; i++) {
            right[i - mid] = arr[i];
        }


        left = mergeSort(left);
        right = mergeSort(right);

        // объединяем
        return merge(left, right);
    }
    int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];

        int i = 0; // индекс левого массива
        int j = 0; // индекс правого массива
        int k = 0; // индекс результата

        while (i < left.length && j < right.length) {

            // сравниваем элементы
            if (left[i] <= right[j]) {
                result[k] = left[i];
                i++;
            } else {
                result[k] = right[j];
                j++;
            }
            k++;
        }

        // если остались элементы слева
        while (i < left.length) {
            result[k] = left[i];
            i++;
            k++;
        }

        // если остались элементы справа
        while (j < right.length) {
            result[k] = right[j];
            j++;
            k++;
        }

        return result;
    }


}
