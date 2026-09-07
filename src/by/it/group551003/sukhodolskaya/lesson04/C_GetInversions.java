package by.it.group551003.sukhodolskaya.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

Sample Input:
5
2 3 9 2 9
Sample Output:
2
*/

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        //запускаем сортировку
        int result = mergeSortAndCount(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    /**
     * Рекурсивная сортировка слиянием с подсчётом инверсий
     * @return количество инверсий в отрезке [left, right]
     */
    private int mergeSortAndCount(int[] arr, int left, int right) {
        int invCount = 0;

        // обработка искл
        if (left >= right) {
            return 0;
        }

        int mid = left + (right - left) / 2;

        //инверсии в левой части
        invCount += mergeSortAndCount(arr, left, mid);

        //инверсии в правой части
        invCount += mergeSortAndCount(arr, mid + 1, right);

        // Считаем инверсии при слиянии
        invCount += mergeAndCount(arr, left, mid, right);

        return invCount;
    }

    /**
     * Слияние двух отсортированных частей + подсчёт инверсий между ними
     */
    private int mergeAndCount(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];

        int i = left;      // указатель на левую часть
        int j = mid + 1;   // указатель на правую часть
        int k = 0;         // указатель на временный массив
        int invCount = 0;  // счётчик инверсий

        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                //нет инверсии
                temp[k++] = arr[i++];
            } else {
                //arr[i] > arr[j] - инверсия
                invCount += (mid - i + 1);
                temp[k++] = arr[j++];
            }
        }

        //копируем остатки
        while (i <= mid) {
            temp[k++] = arr[i++];
        }
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        //возвращаем отсортированный фрагмент обратно в массив
        for (int p = 0; p < temp.length; p++) {
            arr[left + p] = temp[p];
        }

        return invCount;
    }
}