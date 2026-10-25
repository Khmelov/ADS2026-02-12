package by.it.group551003.ogonovsky.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int result = 0;

        if (n > 0) {
            // d[i] - длина наибольшей строго возрастающей подпоследовательности,
            //        заканчивающейся в позиции i
            int[] d = new int[n];

            for (int i = 0; i < n; i++) {
                d[i] = 1; // сама позиция i всегда даёт подпоследовательность длины 1
                for (int j = 0; j < i; j++) {
                    // строгое возрастание: m[j] < m[i]
                    if (m[j] < m[i] && d[j] + 1 > d[i]) {
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