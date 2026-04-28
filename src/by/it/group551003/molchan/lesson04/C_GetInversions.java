package by.it.group551003.molchan.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].
*/
public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
//        long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
//        long finishTime = System.currentTimeMillis();
        System.out.print(result);
//        System.out.println(finishTime - startTime);
    }

    private int mergeSort(int[] arr, int left, int right) {
        if (left >= right) {
            return 0;
        }

        int mid = left + (right - left) / 2;

        // левая часть
        int leftInversions = mergeSort(arr, left, mid);

        // правая часть
        int rightInversions = mergeSort(arr, mid + 1, right);

        // слияние
        int mergeInversions = merge(arr, left, mid, right);

        return leftInversions + rightInversions + mergeInversions;
    }

    private int merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;
        int inversions = 0;

        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k] = arr[i];
                i++;
            } else {
                inversions += (mid - i + 1);
                temp[k] = arr[j];
                j++;
            }
            k++;
        }

        // остаток в левой части
        while (i <= mid) {
            temp[k] = arr[i];
            k++;
            i++;
        }

        // остаток в правой части
        while (j <= right) {
            temp[k] = arr[j];
            k++;
            j++;
        }

        for (int idx = 0; idx < temp.length; idx++) {
            arr[left + idx] = temp[idx];
        }

        return inversions;
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = mergeSort(a, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}