package by.it.group510901.parkhomenko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Решение задачи B: длина подпоследовательности, где каждый следующий элемент делится на предыдущий.
public class B_LongDivComSubSeq {

    // Запускает решение на dataB.txt и выводит найденную длину.
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    // Считывает последовательность и считает длину наибольшей кратной подпоследовательности.
    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
        }

        int[] lengths = new int[n];
        int result = 0;

        // lengths[i] - лучшая длина подходящей подпоследовательности, которая заканчивается в i.
        for (int i = 0; i < n; i++) {
            lengths[i] = 1;
            for (int j = 0; j < i; j++) {
                if (values[i] % values[j] == 0) {
                    lengths[i] = Math.max(lengths[i], lengths[j] + 1);
                }
            }
            result = Math.max(result, lengths[i]);
        }
        return result;
    }
}
