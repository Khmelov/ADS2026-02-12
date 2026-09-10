package by.it.group551001.belozorchik.lesson04;

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
        // подготовка временного массива для слияния
        int[] temp = new int[n];
        System.arraycopy(a, 0, temp, 0, n);
        //откуда, с какого индекса, куда, индекс, сколько

        my_merge_sort(temp, a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }

    private int[] my_merge_sort(int[] from_arr, int[] to_arr, int left, int right) {
        //если в подмассиве 1 эл
        if (left == right) {
            to_arr[left] = from_arr[left];
            return to_arr;
        }
        //разделяй и сортируй
        int middle = left + (right - left) / 2;
        // сортируем половины
        // меняем местами массивы, чтобы результат слияния шел в правильный массив
        my_merge_sort(to_arr, from_arr, left, middle);
        my_merge_sort(to_arr, from_arr,middle + 1, right);

        //слияние двух отсортированных массивов
        int i = left;       // указ на начало левой части
        int j = middle + 1; // правой части
        int k = left;       // для записи в целевой массив

        while (i <= middle && j <= right) {
            if (from_arr[i] <= from_arr[j]) {
                to_arr[k++] = from_arr[i++];
            } else {
                to_arr[k++] = from_arr[j++];
            }
        }

        // дописываем остатки, если они есть
        while (i <= middle) {
            to_arr[k++] = from_arr[i++];
        }
        while (j <= right) {
            to_arr[k++] = from_arr[j++];
        }

        return to_arr;
    }

}

