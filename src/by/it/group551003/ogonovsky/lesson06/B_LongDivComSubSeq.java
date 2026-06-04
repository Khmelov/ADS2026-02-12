package by.it.group551003.ogonovsky.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        long[] m = new long[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextLong();
        }

        int result = 0;

        if (n > 0) {
            // d[i] - длина наибольшей "кратной" подпоследовательности,
            //        заканчивающейся в позиции i
            int[] d = new int[n];

            for (int i = 0; i < n; i++) {
                d[i] = 1; // сама позиция i даёт подпоследовательность длины 1
                for (int j = 0; j < i; j++) {
                    // условие: m[i] делится на m[j]
                    if (m[i] % m[j] == 0 && d[j] + 1 > d[i]) {
                        d[i] = d[j] + 1;
                    }
                }
                if (d[i] > result) {
                    result = d[i];
                }
            }
        }

        return result;
    }
}