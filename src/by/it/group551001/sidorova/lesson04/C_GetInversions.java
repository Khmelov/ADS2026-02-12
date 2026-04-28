package by.it.group551001.sidorova.lesson04;

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

        return mergeSortCount(a, 0, n - 1);
    }

    private int mergeSortCount(int[] a, int left, int right) {
        if (left >= right) return 0;

        int mid = left + (right - left) / 2;

        int inv = 0;
        inv += mergeSortCount(a, left, mid);
        inv += mergeSortCount(a, mid + 1, right);
        inv += merge(a, left, mid, right);

        return inv;
    }

    private int merge(int[] a, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];

        int i = left;
        int j = mid + 1;
        int k = 0;
        int invCount = 0;

        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
                invCount += (mid - i + 1); // все оставшиеся в левой части — инверсии
            }
        }

        while (i <= mid) temp[k++] = a[i++];
        while (j <= right) temp[k++] = a[j++];

        for (int t = 0; t < temp.length; t++) {
            a[left + t] = temp[t];
        }

        return invCount;
    }
}
