package by.it.group551004.podvitelskiymichael.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] tails = new int[n];
        int[] pos = new int[n];
        int[] prev = new int[n];
        int len = 0;

        int[] tailIdx = new int[n];

        for (int i = 0; i < n; i++) {
            prev[i] = -1;
        }

        for (int i = 0; i < n; i++) {

            int lo = 0, hi = len;
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (tails[mid] >= m[i]) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }

            tails[lo] = m[i];
            tailIdx[lo] = i;
            pos[i] = lo + 1;

            if (lo > 0) {
                prev[i] = tailIdx[lo - 1];
            }

            if (lo == len) {
                len++;
            }
        }

        int lastIdx = tailIdx[len - 1];

        ArrayList<Integer> indices = new ArrayList<>();
        for (int cur = lastIdx; cur != -1; cur = prev[cur]) {
            indices.add(cur + 1);
        }
        Collections.reverse(indices);

        StringBuilder sb = new StringBuilder();
        sb.append(len).append("\n");
        for (int i = 0; i < indices.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(indices.get(i));
        }
        System.out.println(sb);

        return len;
    }
}