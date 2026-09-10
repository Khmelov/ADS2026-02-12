package by.it.group551003.ogonovsky.lesson04;

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

        if (n > 1) {
            int[] buffer = new int[n];
            result = mergeSortAndCount(a, buffer, 0, n - 1);
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    // модифицированная сортировка слиянием: заодно считаем инверсии
    private int mergeSortAndCount(int[] a, int[] buffer, int left, int right) {
        if (left >= right) return 0;
        int mid = (left + right) >>> 1;
        int count = 0;
        count += mergeSortAndCount(a, buffer, left, mid);
        count += mergeSortAndCount(a, buffer, mid + 1, right);
        count += mergeAndCount(a, buffer, left, mid, right);
        return count;
    }

    // слияние двух отсортированных половин; когда элемент из правой половины
    // оказывается меньше текущего элемента левой — все оставшиеся элементы
    // левой половины (их mid - i + 1 штук) образуют инверсии с этим элементом
    private int mergeAndCount(int[] a, int[] buffer, int left, int mid, int right) {
        int i = left;
        int j = mid + 1;
        int k = left;
        int count = 0;

        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                buffer[k++] = a[i++];
            } else {
                buffer[k++] = a[j++];
                count += (mid - i + 1);
            }
        }
        while (i <= mid) buffer[k++] = a[i++];
        while (j <= right) buffer[k++] = a[j++];

        for (int t = left; t <= right; t++) {
            a[t] = buffer[t];
        }
        return count;
    }
}