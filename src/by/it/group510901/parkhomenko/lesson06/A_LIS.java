package by.it.group510901.parkhomenko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Решение задачи A: длина наибольшей строго возрастающей подпоследовательности.
public class A_LIS {

    // Запускает решение на dataA.txt и выводит найденную длину.
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    // Считывает последовательность и считает длину LIS динамическим программированием.
    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
        }

        int[] lengths = new int[n];
        int result = 0;

        // lengths[i] - лучшая длина возрастающей подпоследовательности, которая заканчивается в i.
        for (int i = 0; i < n; i++) {
            lengths[i] = 1;
            for (int j = 0; j < i; j++) {
                if (values[j] < values[i]) {
                    lengths[i] = Math.max(lengths[i], lengths[j] + 1);
                }
            }
            result = Math.max(result, lengths[i]);
        }
        return result;
    }
}
