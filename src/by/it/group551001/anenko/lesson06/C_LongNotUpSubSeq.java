package by.it.group551001.anenko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        if (!scanner.hasNextInt()) return 0;

        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int[] tailIndices = new int[n];
        int[] prev = new int[n];
        int len = 0;

        for (int i = 0; i < n; i++) {
            int x = m[i];

            int low = 0;
            int high = len - 1;
            int pos = len;

            while (low <= high) {
                int mid = (low + high) / 2;
                if (m[tailIndices[mid]] < x) {
                    pos = mid;
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }

            if (pos > 0) {
                prev[i] = tailIndices[pos - 1];
            } else {
                prev[i] = -1;
            }

            tailIndices[pos] = i;
            if (pos == len) {
                len++;
            }
        }

        System.out.println(len);

        int[] resultIndices = new int[len];
        int curr = tailIndices[len - 1];
        for (int i = len - 1; i >= 0; i--) {
            resultIndices[i] = curr + 1; // перевод в 1-индексацию
            curr = prev[curr];
        }

        for (int i = 0; i < len; i++) {
            System.out.print(resultIndices[i] + (i == len - 1 ? "" : " "));
        }
        System.out.println();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        return len;
    }
}