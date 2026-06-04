package by.it.group551004.yakhnin.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

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

        int result = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!

        // Временный массив для сортировки
        int[] temp = new int[n];

        // Подсчет инверсий с помощью модифицированной сортировки слиянием
        result = mergeSortAndCount(a, temp, 0, n - 1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        scanner.close();
        return result;
    }

    // Рекурсивная сортировка слиянием и подсчет инверсий
    private int mergeSortAndCount(int[] arr, int[] temp, int left, int right) {
        int inversions = 0;

        if (left < right) {
            int mid = left + (right - left) / 2;

            // Считаем инверсии в левой половине
            inversions += mergeSortAndCount(arr, temp, left, mid);

            // Считаем инверсии в правой половине
            inversions += mergeSortAndCount(arr, temp, mid + 1, right);

            // Считаем инверсии при слиянии
            inversions += mergeAndCount(arr, temp, left, mid, right);
        }

        return inversions;
    }

    // Слияние двух отсортированных половин и подсчет инверсий
    private int mergeAndCount(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left;      // индекс для левой половины
        int j = mid + 1;   // индекс для правой половины
        int k = left;      // индекс для временного массива
        int inversions = 0;

        // Сливаем две половины
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                // Встретили инверсию: arr[i] > arr[j]
                // Все элементы от i до mid больше arr[j]
                temp[k++] = arr[j++];
                inversions += (mid - i + 1);
            }
        }

        // Копируем оставшиеся элементы левой половины
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // Копируем оставшиеся элементы правой половины
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        // Копируем отсортированный фрагмент обратно в исходный массив
        for (i = left; i <= right; i++) {
            arr[i] = temp[i];
        }

        return inversions;
    }
}