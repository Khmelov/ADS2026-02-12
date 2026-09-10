package by.it.group510901.parkhomenko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Решение задачи C: длина наибольшей невозрастающей подпоследовательности.
public class C_LongNotUpSubSeq {

    // Запускает решение на dataC.txt и выводит найденную длину.
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    // Считывает последовательность и считает длину LNIS за O(n log n).
    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] tails = new int[n];
        int size = 0;

        // Меняем знак числа и сводим невозрастающую подпоследовательность к неубывающей.
        for (int i = 0; i < n; i++) {
            int value = -scanner.nextInt();
            int position = upperBound(tails, size, value);
            tails[position] = value;
            if (position == size) {
                size++;
            }
        }
        return size;
    }

    // Возвращает первую позицию, где элемент строго больше target.
    private int upperBound(int[] values, int size, int target) {
        int left = 0;
        int right = size;
        while (left < right) {
            int middle = left + (right - left) / 2;
            if (values[middle] <= target) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }
}
