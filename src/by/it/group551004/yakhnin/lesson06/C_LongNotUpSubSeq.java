package by.it.group551004.yakhnin.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // tails[k] - минимальный последний элемент невозрастающей последовательности длины k+1
        int[] tails = new int[n];
        int length = 0;  // текущая длина LNDS

        for (int i = 0; i < n; i++) {
            // Бинарный поиск: ищем первую позицию, где tails[pos] < a[i]
            int left = 0, right = length;
            while (left < right) {
                int mid = (left + right) / 2;
                if (tails[mid] < a[i]) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }

            int pos = left;
            tails[pos] = a[i];

            if (pos == length) {
                length++;
            }
        }

        scanner.close();
        return length;
    }
}