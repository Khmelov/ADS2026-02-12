package by.it.group551003.stemakhau.lesson04;

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

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        return countInversions(a, 0, n - 1);
    }

    private int countInversions(int[] a, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            count += countInversions(a, left, mid);
            count += countInversions(a, mid + 1, right);
            count += mergeAndCount(a, left, mid, right);
        }
        return count;
    }

    private int mergeAndCount(int[] a, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;
        int inversions = 0;

        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
                // Все оставшиеся элементы в левой части больше a[j]
                inversions += (mid - i + 1);
            }
        }

        while (i <= mid) {
            temp[k++] = a[i++];
        }

        while (j <= right) {
            temp[k++] = a[j++];
        }

        // Копируем обратно в исходный массив
        for (int m = 0; m < temp.length; m++) {
            a[left + m] = temp[m];
        }

        return inversions;
    }
}