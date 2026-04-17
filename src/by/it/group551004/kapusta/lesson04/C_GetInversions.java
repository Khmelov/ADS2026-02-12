package by.it.group551004.kapusta.lesson04;

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

        // размер массива
        int n = scanner.nextInt();

        // сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // запускаем модифицированную сортировку слиянием
        return mergeSortCount(a, 0, n - 1);
    }

    // ------------------ Подсчёт инверсий через merge sort ------------------

    private int mergeSortCount(int[] a, int left, int right) {
        if (left >= right) return 0;

        int mid = (left + right) / 2;

        int inversions = 0;

        inversions += mergeSortCount(a, left, mid);
        inversions += mergeSortCount(a, mid + 1, right);
        inversions += merge(a, left, mid, right);

        return inversions;
    }

    private int merge(int[] a, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];

        int i = left;      // левая часть
        int j = mid + 1;   // правая часть
        int k = 0;

        int inversions = 0;

        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                // ВАЖНО:
                // если a[i] > a[j], то ВСЕ элементы от i до mid
                // образуют инверсии с a[j]
                inversions += (mid - i + 1);
                temp[k++] = a[j++];
            }
        }

        while (i <= mid) temp[k++] = a[i++];
        while (j <= right) temp[k++] = a[j++];

        // копируем обратно
        for (int t = 0; t < temp.length; t++) {
            a[left + t] = temp[t];
        }

        return inversions;
    }
}
