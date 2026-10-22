package by.it.group551001.maxim_belyak.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream =
                C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");

        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();

        int result = instance.getNotUpSeqSize(stream);

        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream)
            throws FileNotFoundException {

        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();

        int[] m = new int[n];

        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }


        int[] dp = new int[n + 1];


        int[] previous = new int[n];

        int length = 0;

        for (int i = 0; i < n; i++) {

            int left = 1;
            int right = length;

            while (left <= right) {

                int mid = (left + right) / 2;

                if (m[dp[mid]] >= m[i]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            int newLength = left;

            previous[i] = newLength > 1
                    ? dp[newLength - 1]
                    : -1;

            dp[newLength] = i;

            if (newLength > length) {
                length = newLength;
            }
        }


        int[] answer = new int[length];

        int k = dp[length];

        for (int i = length - 1; i >= 0; i--) {
            answer[i] = k + 1;
            k = previous[k];
        }

        System.out.println(length);

        for (int i = 0; i < length; i++) {
            System.out.print(answer[i] + " ");
        }

        return length;
    }
}